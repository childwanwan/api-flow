package com.apigateway.application.task;

import com.apigateway.application.task.command.TaskCancelCommand;
import com.apigateway.application.task.command.TaskRetryCommand;
import com.apigateway.application.task.command.TaskSubmitCommand;
import com.apigateway.common.exception.BusinessException;
import com.apigateway.common.exception.ErrorCode;
import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.domain.config.query.ApiConfigQuery;
import com.apigateway.domain.config.repository.ApiConfigRepository;
import com.apigateway.domain.task.enums.ActionType;
import com.apigateway.domain.task.model.RequestContext;
import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.domain.task.query.TaskQuery;
import com.apigateway.domain.task.repository.TaskRepository;
import com.apigateway.domain.task.service.TaskDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskApplicationService {

    private final TaskDomainService taskDomainService;
    private final ApiConfigRepository apiConfigRepository;
    private final TaskRepository taskRepository;

    public TaskEntity submitTask(TaskSubmitCommand command) {
        ApiConfigQuery configQuery = ApiConfigQuery.builder().apiCode(command.getApiCode()).build();
        ApiConfigEntity config = apiConfigRepository.query(configQuery);
        if (config == null || config.getDeleted()) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }
        if (!config.isEnabled()) {
            throw new BusinessException(ErrorCode.API_DISABLED);
        }

        ActionType actionType = ActionType.fromCode(command.getActionType());
        RequestContext requestContext = RequestContext.builder()
                .params(command.getParams())
                .customData(command.getCustomData())
                .build();

        TaskEntity task = taskDomainService.createTask(
                command.getApiCode(),
                config.getApiName(),
                command.getSource(),
                command.getGroupNo(),
                actionType,
                command.getPriority(),
                requestContext,
                command.getReceiptConfig() != null ? command.getReceiptConfig() : config.getReceiptConfig(),
                config.getAutoRetryCount(),
                config.getRetryIntervalMs()
        );

        if (actionType == ActionType.SYNC) {
            task = executeTaskSync(task);
        }

        return task;
    }

    private TaskEntity executeTaskSync(TaskEntity task) {
        task.startExecute();
        try {
            Object responseData = doExecute(task);
            task.complete(responseData);
        } catch (Exception e) {
            log.error("Task execution failed, taskNo: {}", task.getTaskNo(), e);
            task.fail(e.getMessage());
        }
        return taskRepository.update(task);
    }

    private Object doExecute(TaskEntity task) {
        return java.util.Map.of("result", "success", "message", "Task executed successfully");
    }

    public TaskEntity getTask(String taskNo) {
        TaskQuery query = TaskQuery.builder().taskNo(taskNo).build();
        TaskEntity task = taskRepository.query(query);
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        return task;
    }

    public TaskEntity cancelTask(TaskCancelCommand command) {
        return taskDomainService.cancelTask(command.getTaskNo(), command.getReason(), command.getCanceledBy());
    }

    public TaskEntity retryTask(TaskRetryCommand command) {
        return taskDomainService.retryTask(command.getTaskNo());
    }

}
