package com.apigateway.interfaces.task;

import com.apigateway.application.task.TaskApplicationService;
import com.apigateway.application.task.command.TaskCancelCommand;
import com.apigateway.application.task.command.TaskRetryCommand;
import com.apigateway.application.task.command.TaskSubmitCommand;
import com.apigateway.common.result.Result;
import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.interfaces.task.dto.TaskCancelRequest;
import com.apigateway.interfaces.task.dto.TaskRetryRequest;
import com.apigateway.interfaces.task.dto.TaskSubmitRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskApplicationService taskApplicationService;

    @PostMapping("/submit")
    public Result<TaskEntity> submitTask(@Valid @RequestBody TaskSubmitRequest request) {
        log.info("Submit task request: {}", request);
        TaskSubmitCommand command = TaskSubmitCommand.builder()
                .apiCode(request.getApiCode())
                .source(request.getSource())
                .groupNo(request.getGroupNo())
                .actionType(request.getActionType())
                .priority(request.getPriority())
                .params(request.getParams())
                .customData(request.getCustomData())
                .receiptConfig(request.getReceiptConfig())
                .build();
        TaskEntity task = taskApplicationService.submitTask(command);
        return Result.success(task);
    }

    @GetMapping("/{taskNo}")
    public Result<TaskEntity> getTask(@PathVariable String taskNo) {
        log.info("Get task request: taskNo={}", taskNo);
        TaskEntity task = taskApplicationService.getTask(taskNo);
        return Result.success(task);
    }

    @PostMapping("/cancel")
    public Result<TaskEntity> cancelTask(@Valid @RequestBody TaskCancelRequest request) {
        log.info("Cancel task request: {}", request);
        TaskCancelCommand command = TaskCancelCommand.builder()
                .taskNo(request.getTaskNo())
                .reason(request.getCancelReason())
                .canceledBy(request.getCanceledBy())
                .build();
        TaskEntity task = taskApplicationService.cancelTask(command);
        return Result.success(task);
    }

    @PostMapping("/retry")
    public Result<TaskEntity> retryTask(@Valid @RequestBody TaskRetryRequest request) {
        log.info("Retry task request: {}", request);
        TaskRetryCommand command = TaskRetryCommand.builder()
                .taskNo(request.getTaskNo())
                .retryOperator(request.getRetryOperator())
                .build();
        TaskEntity task = taskApplicationService.retryTask(command);
        return Result.success(task);
    }

}
