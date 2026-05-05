package com.flow.interfaces.biz.task;

import com.flow.application.task.TaskApplicationService;
import com.flow.application.task.command.TaskCancelCommand;
import com.flow.application.task.command.TaskRetryCommand;
import com.flow.application.task.command.TaskSubmitCommand;
import com.flow.common.result.Result;
import com.flow.domain.task.model.TaskDO;
import com.flow.interfaces.biz.task.dto.TaskCancelRequest;
import com.flow.interfaces.biz.task.dto.TaskRetryRequest;
import com.flow.interfaces.biz.task.dto.TaskSubmitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskApplicationService taskApplicationService;

    @PostMapping("/submit")
    public Result<TaskDO> submitTask(@RequestBody TaskSubmitRequest request) {
        validateTaskSubmitRequest(request);
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
        TaskDO task = taskApplicationService.submitTask(command);
        return Result.success(task);
    }

    @GetMapping("/{taskNo}")
    public Result<TaskDO> getTask(@PathVariable String taskNo) {
        validateTaskNo(taskNo);
        log.info("Get task request: taskNo={}", taskNo);
        TaskDO task = taskApplicationService.getTask(taskNo);
        return Result.success(task);
    }

    @PostMapping("/cancel")
    public Result<TaskDO> cancelTask(@RequestBody TaskCancelRequest request) {
        validateTaskCancelRequest(request);
        log.info("Cancel task request: {}", request);
        TaskCancelCommand command = TaskCancelCommand.builder()
                .taskNo(request.getTaskNo())
                .reason(request.getCancelReason())
                .canceledBy(request.getCanceledBy())
                .build();
        TaskDO task = taskApplicationService.cancelTask(command);
        return Result.success(task);
    }

    @PostMapping("/retry")
    public Result<TaskDO> retryTask(@RequestBody TaskRetryRequest request) {
        validateTaskRetryRequest(request);
        log.info("Retry task request: {}", request);
        TaskRetryCommand command = TaskRetryCommand.builder()
                .taskNo(request.getTaskNo())
                // todo 操作人应该直接后端取token
                // .retryOperator(request.getRetryOperator())
                .build();
        TaskDO task = taskApplicationService.retryTask(command);
        return Result.success(task);
    }


    private void validateTaskSubmitRequest(TaskSubmitRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalArgumentException("request cannot be null");
        }
        request.validate();
    }

    private void validateTaskCancelRequest(TaskCancelRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalArgumentException("request cannot be null");
        }
        request.validate();
    }

    private void validateTaskRetryRequest(TaskRetryRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalArgumentException("request cannot be null");
        }
        request.validate();
    }

    private void validateTaskNo(String taskNo) {
        if (StringUtils.isBlank(taskNo)) {
            throw new IllegalArgumentException("taskNo cannot be blank");
        }
    }

}