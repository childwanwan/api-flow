package com.apiflow.application.task;

import cn.hutool.core.util.StrUtil;
import com.apiflow.common.enums.ActionType;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.api.repository.config.param.ApiConfigField;
import com.apiflow.api.repository.config.param.SelectOneApiConfigParam;
import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.api.repository.task.TaskRepository;
import com.apiflow.api.repository.task.idto.TaskIDTO;
import com.apiflow.api.repository.task.param.SelectTaskParam;
import com.apiflow.application.task.param.CancelTaskParam;
import com.apiflow.application.task.param.ListTaskParam;
import com.apiflow.application.task.param.RetryTaskParam;
import com.apiflow.application.task.param.SubmitTaskParam;
import com.apiflow.application.task.converter.TaskConverter;
import com.apiflow.application.task.dto.*;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.domain.config.converter.ApiConfigConverter;
import com.apiflow.domain.config.model.ApiConfig;
import com.apiflow.domain.config.model.PluginChainItem;
import com.apiflow.domain.config.model.PluginConfig;
import com.apiflow.domain.plugin.PluginChainExecutor;
import com.apiflow.domain.plugin.PluginContext;
import com.apiflow.domain.scheduler.TaskWakeUpSignal;
import com.apiflow.domain.task.model.ExecInfo;
import com.apiflow.domain.task.model.ReceiptConfig;
import com.apiflow.domain.task.model.Task;
import com.apiflow.domain.task.service.TaskDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskApplicationService {

    private static final ApiConfigConverter API_CONFIG_CONVERTER = ApiConfigConverter.INSTANCE;
    private static final TaskConverter CONVERTER = TaskConverter.INSTANCE;

    private final TaskDomainService taskDomainService;
    private final ApiConfigRepository apiConfigRepository;
    private final TaskRepository taskRepository;
    private final PluginChainExecutor pluginChainExecutor;
    private final TaskWakeUpSignal taskWakeUpSignal;
    private final TransactionTemplate transactionTemplate;

    public TaskDTO submitTask(SubmitTaskParam param) {
        SelectOneApiConfigParam configParam = SelectOneApiConfigParam.builder()
                .condition(ConditionNode.eq(ApiConfigField.API_CODE.getFieldName(), param.getApiCode())).build();
        ApiConfig config = API_CONFIG_CONVERTER.apiConfigIDTOToApiConfig(apiConfigRepository.selectOne(configParam));
        if (config == null || Boolean.TRUE.equals(config.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }
        if (!config.isEnabled()) {
            throw new BusinessException(ErrorCode.API_DISABLED);
        }

        ReceiptConfig receiptConfig = config.getReceiptConfig();
        if (param.getReceiptConfig() != null) {
            receiptConfig = CONVERTER.toReceiptConfig(param.getReceiptConfig());
        }
        ReceiptConfig effectiveReceiptConfig = receiptConfig;

        Task task = transactionTemplate.execute(status -> {
            Task t = taskDomainService.createTask(
                    param.getApiCode(),
                    config.getApiName(),
                    param.getSource(),
                    param.getGroupNo(),
                    param.getActionType(),
                    param.getPriority(),
                    param.getCustomData(),
                    effectiveReceiptConfig,
                    config.getAutoRetryCount(),
                    config.getRetryIntervalMs()
            );

            if (ActionType.SYNC.getValue().equals(param.getActionType())) {
                t = executeTaskSync(t);
            }

            return t;
        });

        taskWakeUpSignal.setSignal();

        return CONVERTER.toDTO(task);
    }

    private Task executeTaskSync(Task task) {
        task.startExecute();
        try {
            SelectOneApiConfigParam configParam = SelectOneApiConfigParam.builder()
                    .condition(ConditionNode.eq(ApiConfigField.API_CODE.getFieldName(), task.getApiCode())).build();
            ApiConfig config = API_CONFIG_CONVERTER.apiConfigIDTOToApiConfig(apiConfigRepository.selectOne(configParam));

            ExecInfo execInfo = executePluginChain(task, config);
            String responseData = execInfo != null ? JsonUtil.toJson(Map.of("result", "success", "costTimeMs", execInfo.getTotalCostTimeMs())) : JsonUtil.toJson(Map.of("result", "success"));
            return taskDomainService.completeTask(task.getTaskNo(), responseData);
        } catch (Exception e) {
            log.error("Task execution failed, taskNo: {}", task.getTaskNo(), e);
            return taskDomainService.failTask(task.getTaskNo());
        }
    }

    private ExecInfo executePluginChain(Task task, ApiConfig config) {
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
        if (StrUtil.isEmpty(json)) {
            return null;
        }
        try {
            return JsonUtil.toObject(json, Map.class);
        } catch (Exception e) {
            log.warn("Failed to parse json to map: {}", json, e);
            return null;
        }
    }

    private List<PluginChainExecutor.PluginChainItemConfig> buildChainItems(ApiConfig config) {
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
        Task task = taskDomainService.getTask(taskNo);
        return CONVERTER.toDTO(task);
    }

    public TaskDTO cancelTask(CancelTaskParam param) {
        Task task = transactionTemplate.execute(status ->
                taskDomainService.cancelTask(param.getTaskNo(), param.getReason(), param.getCanceledBy()));
        return CONVERTER.toDTO(task);
    }

    public TaskDTO retryTask(RetryTaskParam param) {
        Task task = transactionTemplate.execute(status ->
                taskDomainService.retryTask(param.getTaskNo()));
        taskWakeUpSignal.setSignal();
        return CONVERTER.toDTO(task);
    }

    public List<TaskDTO> listTasks(ListTaskParam param) {
        SelectTaskParam.SelectTaskParamBuilder builder = SelectTaskParam.builder();
        if (StrUtil.isNotEmpty(param.getTaskNo())) {
            builder.taskNo(FieldCondition.<String>builder().like(param.getTaskNo()).build());
        }
        if (StrUtil.isNotEmpty(param.getApiCode())) {
            builder.apiCode(FieldCondition.of(param.getApiCode()));
        }
        if (StrUtil.isNotEmpty(param.getStatus())) {
            builder.status(FieldCondition.of(param.getStatus()));
        }
        builder.limit(param.getLimit() != null ? param.getLimit() : 100);
        List<TaskIDTO> tasks = taskRepository.selectList(builder.build());
        return CONVERTER.toDTOListFromIDTO(tasks);
    }
}
