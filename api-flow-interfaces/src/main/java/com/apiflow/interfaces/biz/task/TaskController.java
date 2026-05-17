package com.apiflow.interfaces.biz.task;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.apiflow.application.task.TaskApplicationService;
import com.apiflow.application.task.command.TaskCancelCommand;
import com.apiflow.application.task.command.TaskRetryCommand;
import com.apiflow.application.task.command.TaskSubmitCommand;
import com.apiflow.application.task.dto.ReceiptConfigDTO;
import com.apiflow.application.task.dto.TaskDTO;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.annotation.OpLog;
import com.apiflow.interfaces.biz.task.request.TaskCancelRequest;
import com.apiflow.interfaces.biz.task.request.TaskRetryRequest;
import com.apiflow.interfaces.biz.task.request.TaskSubmitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskApplicationService taskApplicationService;

    @PostMapping("/submit")
    @OpLog(module = "任务管理", operation = "提交任务", detail = "提交新任务")
    public Result<TaskDTO> submitTask(@RequestBody TaskSubmitRequest request) {
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
                .receiptConfig(request.getReceiptConfig() != null ? toReceiptConfigDTO(request.getReceiptConfig()) : null)
                .build();
        TaskDTO task = taskApplicationService.submitTask(command);
        return Result.success(task);
    }

    @GetMapping("/{taskNo}")
    public Result<TaskDTO> getTask(@PathVariable String taskNo) {
        validateTaskNo(taskNo);
        log.info("Get task request: taskNo={}", taskNo);
        TaskDTO task = taskApplicationService.getTask(taskNo);
        return Result.success(task);
    }

    @GetMapping("/list")
    public Result<List<TaskDTO>> listTasks(
            @RequestParam(required = false) String taskNo,
            @RequestParam(required = false) String apiCode,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer limit) {
        log.info("List tasks request: taskNo={}, apiCode={}, status={}, limit={}", taskNo, apiCode, status, limit);
        List<TaskDTO> tasks = taskApplicationService.listTasks(taskNo, apiCode, status, limit);
        return Result.success(tasks);
    }

    @PostMapping("/cancel")
    @OpLog(module = "任务管理", operation = "取消任务", detail = "取消任务")
    public Result<TaskDTO> cancelTask(@RequestBody TaskCancelRequest request) {
        validateTaskCancelRequest(request);
        log.info("Cancel task request: {}", request);
        TaskCancelCommand command = TaskCancelCommand.builder()
                .taskNo(request.getTaskNo())
                .reason(request.getCancelReason())
                .canceledBy(request.getCanceledBy())
                .build();
        TaskDTO task = taskApplicationService.cancelTask(command);
        return Result.success(task);
    }

    @PostMapping("/retry")
    @OpLog(module = "任务管理", operation = "重试任务", detail = "重试任务")
    public Result<TaskDTO> retryTask(@RequestBody TaskRetryRequest request) {
        validateTaskRetryRequest(request);
        log.info("Retry task request: {}", request);
        TaskRetryCommand command = TaskRetryCommand.builder()
                .taskNo(request.getTaskNo())
                .build();
        TaskDTO task = taskApplicationService.retryTask(command);
        return Result.success(task);
    }

    private ReceiptConfigDTO toReceiptConfigDTO(Object receiptConfig) {
        if (receiptConfig instanceof ReceiptConfigDTO dto) {
            return dto;
        }
        return null;
    }

    private void validateTaskSubmitRequest(TaskSubmitRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new IllegalArgumentException("request cannot be null");
        }
        request.validate();
    }

    private void validateTaskCancelRequest(TaskCancelRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new IllegalArgumentException("request cannot be null");
        }
        request.validate();
    }

    private void validateTaskRetryRequest(TaskRetryRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new IllegalArgumentException("request cannot be null");
        }
        request.validate();
    }

    private void validateTaskNo(String taskNo) {
        if (StrUtil.isBlank(taskNo)) {
            throw new IllegalArgumentException("taskNo cannot be blank");
        }
    }

}
