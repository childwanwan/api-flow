package com.apiflow.interfaces.biz.task;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.apiflow.application.task.TaskApplicationService;
import com.apiflow.application.task.dto.ReceiptConfigDTO;
import com.apiflow.application.task.dto.TaskDTO;
import com.apiflow.application.task.param.CancelTaskParam;
import com.apiflow.application.task.param.ListTaskParam;
import com.apiflow.application.task.param.RetryTaskParam;
import com.apiflow.application.task.param.SubmitTaskParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.result.Result;
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
    public Result<TaskDTO> submitTask(@RequestBody TaskSubmitRequest request) {
        validateTaskSubmitRequest(request);
        log.info("Submit task request: {}", request);
        SubmitTaskParam submitParam = SubmitTaskParam.builder()
                .apiCode(request.getApiCode())
                .source(request.getSource())
                .groupNo(request.getGroupNo())
                .actionType(request.getActionType())
                .priority(request.getPriority())
                .params(request.getParams())
                .customData(request.getCustomData())
                .receiptConfig(request.getReceiptConfig() != null ? toReceiptConfigDTO(request.getReceiptConfig()) : null)
                .build();
        TaskDTO task = taskApplicationService.submitTask(submitParam);
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
        ListTaskParam param = ListTaskParam.builder()
                .taskNo(taskNo)
                .apiCode(apiCode)
                .status(status)
                .limit(limit)
                .build();
        List<TaskDTO> tasks = taskApplicationService.listTasks(param);
        return Result.success(tasks);
    }

    @PostMapping("/cancel")
    public Result<TaskDTO> cancelTask(@RequestBody TaskCancelRequest request) {
        validateTaskCancelRequest(request);
        log.info("Cancel task request: {}", request);
        CancelTaskParam cancelParam = CancelTaskParam.builder()
                .taskNo(request.getTaskNo())
                .reason(request.getCancelReason())
                .canceledBy(request.getCanceledBy())
                .build();
        TaskDTO task = taskApplicationService.cancelTask(cancelParam);
        return Result.success(task);
    }

    @PostMapping("/retry")
    public Result<TaskDTO> retryTask(@RequestBody TaskRetryRequest request) {
        validateTaskRetryRequest(request);
        log.info("Retry task request: {}", request);
        RetryTaskParam retryParam = RetryTaskParam.builder()
                .taskNo(request.getTaskNo())
                .build();
        TaskDTO task = taskApplicationService.retryTask(retryParam);
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
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        request.validate();
    }

    private void validateTaskCancelRequest(TaskCancelRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        request.validate();
    }

    private void validateTaskRetryRequest(TaskRetryRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        request.validate();
    }

    private void validateTaskNo(String taskNo) {
        if (StrUtil.isBlank(taskNo)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
    }

}
