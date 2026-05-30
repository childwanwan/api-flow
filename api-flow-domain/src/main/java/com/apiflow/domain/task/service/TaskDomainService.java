package com.apiflow.domain.task.service;

import com.apiflow.common.enums.CompensateStatus;
import com.apiflow.common.enums.TaskStatus;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.api.repository.task.idto.TaskIDTO;
import com.apiflow.api.repository.task.param.SelectOneTaskParam;
import com.apiflow.api.repository.task.TaskRepository;
import com.apiflow.common.constant.SystemConstant;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.domain.task.converter.TaskConverter;
import com.apiflow.domain.task.model.ReceiptConfig;
import com.apiflow.domain.task.model.RequestContext;
import com.apiflow.domain.task.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
public class TaskDomainService {

    private final TaskRepository taskRepository;
    private static final TaskConverter TASK_CONVERTER = TaskConverter.INSTANCE;
    private static final Random RANDOM = new Random();

    public Task createTask(String apiCode, String apiName, String source, String groupNo,
                           String actionType, Integer priority, Object requestData,
                           ReceiptConfig receiptConfig, Integer maxRetryCount, Long retryIntervalMs) {

        long now = System.currentTimeMillis();
        String taskNo = generateTaskNo();

        RequestContext requestContext = RequestContext.builder()
                .customData(requestData instanceof String ? (String) requestData : JsonUtil.toJson(requestData))
                .build();

        Task task = Task.builder()
                .taskNo(taskNo)
                .source(source)
                .groupNo(groupNo)
                .apiCode(apiCode)
                .apiName(apiName)
                .actionType(actionType)
                .status(TaskStatus.PENDING.getValue())
                .interruptFlag(false)
                .compensateStatus(CompensateStatus.NONE.getValue())
                .compensateRetryCount(0)
                .priority(priority == null ? 10 : priority)
                .requestContext(requestContext)
                .receiptConfig(receiptConfig)
                .retryCount(0)
                .maxRetryCount(maxRetryCount == null ? SystemConstant.DEFAULT_AUTO_RETRY_COUNT : maxRetryCount)
                .createTimeMs(now)
                .updateTimeMs(now)
                .version(0)
                .build();

        taskRepository.save(TASK_CONVERTER.taskToSaveTaskParam(task));
        SelectOneTaskParam queryParam = SelectOneTaskParam.builder()
                .taskNo(FieldCondition.of(taskNo)).build();
        TaskIDTO savedDto = taskRepository.selectOne(queryParam);
        return TASK_CONVERTER.taskIDTOToTask(savedDto);
    }

    public Task cancelTask(String taskNo, String reason, String cancelBy) {
        SelectOneTaskParam param = SelectOneTaskParam.builder()
                .taskNo(FieldCondition.of(taskNo)).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        Task task = TASK_CONVERTER.taskIDTOToTask(taskDto);
        if (!task.canCancel()) {
            throw new BusinessException(ErrorCode.TASK_STATUS_NOT_ALLOWED);
        }
        task.cancel(reason, cancelBy);

        taskRepository.update(TASK_CONVERTER.taskToUpdateTaskParam(task));
        TaskIDTO updatedDto = taskRepository.selectOne(param);
        return TASK_CONVERTER.taskIDTOToTask(updatedDto);
    }

    public Task retryTask(String taskNo) {
        SelectOneTaskParam param = SelectOneTaskParam.builder()
                .taskNo(FieldCondition.of(taskNo)).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        Task task = TASK_CONVERTER.taskIDTOToTask(taskDto);
        if (!task.canRetry()) {
            throw new BusinessException(ErrorCode.TASK_STATUS_NOT_ALLOWED);
        }
        task.prepareRetry(SystemConstant.DEFAULT_RETRY_INTERVAL_MS);

        taskRepository.update(TASK_CONVERTER.taskToUpdateTaskParam(task));
        TaskIDTO updatedDto = taskRepository.selectOne(param);
        return TASK_CONVERTER.taskIDTOToTask(updatedDto);
    }

    public Task startExecute(String taskNo) {
        SelectOneTaskParam param = SelectOneTaskParam.builder()
                .taskNo(FieldCondition.of(taskNo)).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        Task task = TASK_CONVERTER.taskIDTOToTask(taskDto);
        task.startExecute();

        taskRepository.update(TASK_CONVERTER.taskToUpdateTaskParam(task));
        TaskIDTO updatedDto = taskRepository.selectOne(param);
        return TASK_CONVERTER.taskIDTOToTask(updatedDto);
    }

    public Task completeTask(String taskNo, String responseData) {
        SelectOneTaskParam param = SelectOneTaskParam.builder()
                .taskNo(FieldCondition.of(taskNo)).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        Task task = TASK_CONVERTER.taskIDTOToTask(taskDto);
        task.complete(responseData);

        taskRepository.update(TASK_CONVERTER.taskToUpdateTaskParam(task));
        TaskIDTO updatedDto = taskRepository.selectOne(param);
        return TASK_CONVERTER.taskIDTOToTask(updatedDto);
    }

    public Task failTask(String taskNo) {
        SelectOneTaskParam param = SelectOneTaskParam.builder()
                .taskNo(FieldCondition.of(taskNo)).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        Task task = TASK_CONVERTER.taskIDTOToTask(taskDto);
        task.fail();

        taskRepository.update(TASK_CONVERTER.taskToUpdateTaskParam(task));
        TaskIDTO updatedDto = taskRepository.selectOne(param);
        return TASK_CONVERTER.taskIDTOToTask(updatedDto);
    }

    public Task getTask(String taskNo) {
        SelectOneTaskParam param = SelectOneTaskParam.builder()
                .taskNo(FieldCondition.of(taskNo)).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        return TASK_CONVERTER.taskIDTOToTask(taskDto);
    }

    private String generateTaskNo() {
        String timestamp = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", System.currentTimeMillis());
        int random = RANDOM.nextInt(90000) + 10000;
        String randomStr = String.valueOf(random);
        return "TASK" + timestamp + randomStr;
    }

}
