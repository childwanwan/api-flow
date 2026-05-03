package com.apigateway.domain.task.service;

import com.apigateway.common.constant.SystemConstant;
import com.apigateway.common.exception.BusinessException;
import com.apigateway.common.exception.ErrorCode;
import com.apigateway.domain.task.enums.ActionType;
import com.apigateway.domain.task.enums.CompensateStatus;
import com.apigateway.domain.task.enums.TaskStatus;
import com.apigateway.domain.task.model.ExecInfo;
import com.apigateway.domain.task.model.RequestContext;
import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.domain.task.query.TaskQuery;
import com.apigateway.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskDomainService {

    private final TaskRepository taskRepository;
    private static final Random RANDOM = new Random();

    public TaskEntity createTask(String apiCode, String apiName, String source, String groupNo,
                                  ActionType actionType, Integer priority, RequestContext requestContext,
                                  String receiptConfig, Integer maxRetryCount, Long retryIntervalMs) {

        long now = System.currentTimeMillis();
        String taskNo = generateTaskNo();

        TaskEntity task = TaskEntity.builder()
                .taskNo(taskNo)
                .source(source)
                .groupNo(groupNo)
                .apiCode(apiCode)
                .apiName(apiName)
                .actionType(actionType)
                .status(TaskStatus.PENDING)
                .interruptFlag(false)
                .compensateStatus(CompensateStatus.NONE)
                .compensateRetryCount(0)
                .priority(priority == null ? 10 : priority)
                .requestContext(requestContext)
                .execInfo(ExecInfo.builder().retryCount(0).build())
                .receiptConfig(receiptConfig)
                .retryCount(0)
                .maxRetryCount(maxRetryCount == null ? SystemConstant.DEFAULT_AUTO_RETRY_COUNT : maxRetryCount)
                .createTimeMs(now)
                .updateTimeMs(now)
                .deleted(false)
                .version(0)
                .build();

        return taskRepository.save(task);
    }

    public TaskEntity cancelTask(String taskNo, String reason, String cancelBy) {
        TaskQuery query = TaskQuery.builder().taskNo(taskNo).build();
        TaskEntity task = taskRepository.query(query);
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        if (!task.canCancel()) {
            throw new BusinessException(ErrorCode.TASK_STATUS_NOT_ALLOWED);
        }
        task.cancel(reason, cancelBy);
        return taskRepository.update(task);
    }

    public TaskEntity retryTask(String taskNo) {
        TaskQuery query = TaskQuery.builder().taskNo(taskNo).build();
        TaskEntity task = taskRepository.query(query);
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        if (!task.canRetry()) {
            throw new BusinessException(ErrorCode.TASK_STATUS_NOT_ALLOWED);
        }
        task.prepareRetry();
        return taskRepository.update(task);
    }

    private String generateTaskNo() {
        String timestamp = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", System.currentTimeMillis());
        int random = RANDOM.nextInt(90000) + 10000;
        String randomStr = String.valueOf(random);
        return "TASK" + timestamp + randomStr;
    }

}
