package com.apiflow.application.task;

import com.apiflow.common.enums.ActionType;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.api.repository.config.param.SelectOneApiConfigParam;
import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.api.repository.task.TaskRepository;
import com.apiflow.api.repository.task.idto.TaskIDTO;
import com.apiflow.api.repository.task.param.SelectTaskParam;
import com.apiflow.application.task.command.TaskCancelCommand;
import com.apiflow.application.task.command.TaskRetryCommand;
import com.apiflow.application.task.command.TaskSubmitCommand;
import com.apiflow.application.task.dto.*;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.domain.config.converter.ApiConfigConverter;
import com.apiflow.domain.config.model.ApiConfigDO;
import com.apiflow.domain.config.model.PluginChainItem;
import com.apiflow.domain.config.model.PluginConfig;
import com.apiflow.domain.plugin.PluginChainExecutor;
import com.apiflow.domain.plugin.PluginContext;
import com.apiflow.domain.scheduler.TaskWakeUpSignal;
import com.apiflow.domain.scheduler.TaskChangeEvent;
import com.apiflow.domain.task.model.ExecInfo;
import com.apiflow.domain.task.model.ReceiptConfig;
import com.apiflow.domain.task.model.TaskDO;
import com.apiflow.domain.task.service.TaskDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskApplicationService {

    private final TaskDomainService taskDomainService;
    private final ApiConfigRepository apiConfigRepository;
    private final TaskRepository taskRepository;
    private final PluginChainExecutor pluginChainExecutor;
    private final ApplicationEventPublisher eventPublisher;
    private final TaskWakeUpSignal taskWakeUpSignal;
    private static final ApiConfigConverter API_CONFIG_CONVERTER = ApiConfigConverter.INSTANCE;

    public TaskDTO submitTask(TaskSubmitCommand command) {
        SelectOneApiConfigParam configParam = SelectOneApiConfigParam.builder()
                .apiCode(FieldCondition.of(command.getApiCode())).build();
        ApiConfigDO config = API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(apiConfigRepository.selectOne(configParam));
        if (config == null || Boolean.TRUE.equals(config.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }
        if (!config.isEnabled()) {
            throw new BusinessException(ErrorCode.API_DISABLED);
        }

        ReceiptConfig receiptConfig = config.getReceiptConfig();
        if (command.getReceiptConfig() != null) {
            receiptConfig = toReceiptConfig(command.getReceiptConfig());
        }

        TaskDO task = taskDomainService.createTask(
                command.getApiCode(),
                config.getApiName(),
                command.getSource(),
                command.getGroupNo(),
                command.getActionType(),
                command.getPriority(),
                command.getCustomData(),
                receiptConfig,
                config.getAutoRetryCount(),
                config.getRetryIntervalMs()
        );

        if (ActionType.SYNC.getValue().equals(command.getActionType())) {
            task = executeTaskSync(task);
        }

        eventPublisher.publishEvent(TaskChangeEvent.submitted(this, task.getTaskNo()));
        taskWakeUpSignal.setSignal();

        return toDTO(task);
    }

    private TaskDO executeTaskSync(TaskDO task) {
        task.startExecute();
        try {
            SelectOneApiConfigParam configParam = SelectOneApiConfigParam.builder()
                    .apiCode(FieldCondition.of(task.getApiCode())).build();
            ApiConfigDO config = API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(apiConfigRepository.selectOne(configParam));

            ExecInfo execInfo = executePluginChain(task, config);
            String responseData = execInfo != null ? JsonUtil.toJson(Map.of("result", "success", "costTimeMs", execInfo.getTotalCostTimeMs())) : JsonUtil.toJson(Map.of("result", "success"));
            return taskDomainService.completeTask(task.getTaskNo(), responseData);
        } catch (Exception e) {
            log.error("Task execution failed, taskNo: {}", task.getTaskNo(), e);
            return taskDomainService.failTask(task.getTaskNo());
        }
    }

    private ExecInfo executePluginChain(TaskDO task, ApiConfigDO config) {
        Map<String, Object> params = null;
        Map<String, Object> customData = null;
        if (task.getRequestContext() != null) {
            params = parseJsonToMap(task.getRequestContext().getParams());
            customData = parseJsonToMap(task.getRequestContext().getCustomData());
        }

        PluginContext context = PluginContext.of(
                task.getTaskNo(),
                task.getApiCode(),
                task.getApiName(),
                params,
                customData,
                config != null ? config.getExtraConfig() : null
        );

        List<PluginChainExecutor.PluginChainItemConfig> chainItems = buildChainItems(config);

        return pluginChainExecutor.execute(context, chainItems);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJsonToMap(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(json, Map.class);
        } catch (Exception e) {
            log.warn("Failed to parse json to map: {}", json, e);
            return null;
        }
    }

    private List<PluginChainExecutor.PluginChainItemConfig> buildChainItems(ApiConfigDO config) {
        if (config != null && config.getPluginConfig() != null
                && Boolean.TRUE.equals(config.getPluginConfig().getEnabled())
                && config.getPluginConfig().getPluginChain() != null) {
            return config.getPluginConfig().getPluginChain().stream()
                    .map(item -> new PluginChainExecutor.PluginChainItemConfig(
                            item.getPluginCode(), item.getOrder(),
                            Boolean.TRUE.equals(item.getEnabled()), item.getConfig()))
                    .toList();
        }
        return List.of(
                new PluginChainExecutor.PluginChainItemConfig("PARAM_VALIDATOR", 1, true, null),
                new PluginChainExecutor.PluginChainItemConfig("RATE_LIMIT_CHECK", 2, true, null),
                new PluginChainExecutor.PluginChainItemConfig("BUSINESS_EXECUTOR", 3, true, null)
        );
    }

    public TaskDTO getTask(String taskNo) {
        TaskDO task = taskDomainService.getTask(taskNo);
        return toDTO(task);
    }

    public TaskDTO cancelTask(TaskCancelCommand command) {
        TaskDO task = taskDomainService.cancelTask(command.getTaskNo(), command.getReason(), command.getCanceledBy());
        return toDTO(task);
    }

    public TaskDTO retryTask(TaskRetryCommand command) {
        TaskDO task = taskDomainService.retryTask(command.getTaskNo());
        eventPublisher.publishEvent(TaskChangeEvent.retried(this, task.getTaskNo()));
        taskWakeUpSignal.setSignal();
        return toDTO(task);
    }

    public List<TaskDTO> listTasks(String taskNo, String apiCode, String status, Integer limit) {
        SelectTaskParam.SelectTaskParamBuilder builder = SelectTaskParam.builder();
        if (taskNo != null && !taskNo.isEmpty()) {
            builder.taskNo(FieldCondition.<String>builder().like(taskNo).build());
        }
        if (apiCode != null && !apiCode.isEmpty()) {
            builder.apiCode(FieldCondition.of(apiCode));
        }
        if (status != null && !status.isEmpty()) {
            builder.status(FieldCondition.of(status));
        }
        if (limit != null) {
            builder.limit(limit);
        } else {
            builder.limit(100);
        }
        List<TaskIDTO> tasks = taskRepository.selectList(builder.build());
        return tasks.stream().map(this::toDTOFromIDTO).toList();
    }

    private TaskDTO toDTOFromIDTO(TaskIDTO dto) {
        return TaskDTO.builder()
                .id(dto.getId())
                .taskNo(dto.getTaskNo())
                .source(dto.getSource())
                .groupNo(dto.getGroupNo())
                .apiCode(dto.getApiCode())
                .apiName(dto.getApiName())
                .actionType(dto.getActionType())
                .status(dto.getStatus())
                .interruptFlag(dto.getInterruptFlag())
                .compensateStatus(dto.getCompensateStatus())
                .priority(dto.getPriority())
                .retryCount(dto.getRetryCount())
                .maxRetryCount(dto.getMaxRetryCount())
                .createTimeMs(dto.getCreateTimeMs())
                .startExecuteTimeMs(dto.getStartExecuteTimeMs())
                .endExecuteTimeMs(dto.getEndExecuteTimeMs())
                .executeDurationMs(dto.getExecuteDurationMs())
                .updateTimeMs(dto.getUpdateTimeMs())
                .build();
    }

    private TaskDTO toDTO(TaskDO task) {
        return TaskDTO.builder()
                .id(task.getId())
                .taskNo(task.getTaskNo())
                .source(task.getSource())
                .groupNo(task.getGroupNo())
                .apiCode(task.getApiCode())
                .apiName(task.getApiName())
                .actionType(task.getActionType())
                .status(task.getStatus())
                .interruptFlag(task.getInterruptFlag())
                .compensateStatus(task.getCompensateStatus())
                .compensateRetryCount(task.getCompensateRetryCount())
                .compensateNextRetryTimeMs(task.getCompensateNextRetryTimeMs())
                .priority(task.getPriority())
                .requestContext(toRequestContextDTO(task.getRequestContext()))
                .execInfo(toExecInfoDTO(task.getExecInfo()))
                .responseData(task.getResponseData())
                .expireTimeMs(task.getExpireTimeMs())
                .receiptConfig(toReceiptConfigDTO(task.getReceiptConfig()))
                .receiptInfo(toReceiptInfoDTO(task.getReceiptInfo()))
                .retryCount(task.getRetryCount())
                .maxRetryCount(task.getMaxRetryCount())
                .nextRetryTimeMs(task.getNextRetryTimeMs())
                .createTimeMs(task.getCreateTimeMs())
                .startExecuteTimeMs(task.getStartExecuteTimeMs())
                .endExecuteTimeMs(task.getEndExecuteTimeMs())
                .executeDurationMs(task.getExecuteDurationMs())
                .cancelTimeMs(task.getCancelTimeMs())
                .cancelReason(task.getCancelReason())
                .canceledBy(task.getCanceledBy())
                .updateTimeMs(task.getUpdateTimeMs())
                .build();
    }

    private RequestContextDTO toRequestContextDTO(com.apiflow.domain.task.model.RequestContext context) {
        if (context == null) return null;
        return RequestContextDTO.builder()
                .params(context.getParams())
                .customData(context.getCustomData())
                .build();
    }

    private ExecInfoDTO toExecInfoDTO(com.apiflow.domain.task.model.ExecInfo info) {
        if (info == null) return null;
        return ExecInfoDTO.builder()
                .totalCostTimeMs(info.getTotalCostTimeMs())
                .retryCount(info.getRetryCount())
                .build();
    }

    private ReceiptConfigDTO toReceiptConfigDTO(ReceiptConfig config) {
        if (config == null) return null;
        return ReceiptConfigDTO.builder()
                .receiptTypes(config.getReceiptTypes())
                .build();
    }

    private ReceiptInfoDTO toReceiptInfoDTO(com.apiflow.domain.task.model.ReceiptInfo info) {
        if (info == null) return null;
        return ReceiptInfoDTO.builder().build();
    }

    private ReceiptConfig toReceiptConfig(ReceiptConfigDTO dto) {
        if (dto == null) return null;
        return ReceiptConfig.builder()
                .receiptTypes(dto.getReceiptTypes())
                .build();
    }

}
