package com.flow.domain.task.service;

import com.flow.api.repository.task.idto.TaskIDTO;
import com.flow.api.repository.task.idto.TaskReceiptConfigIDTO;
import com.flow.api.repository.task.idto.TaskRequestContextIDTO;
import com.flow.api.repository.task.param.SelectOneTaskParam;
import com.flow.api.repository.task.TaskRepository;
import com.flow.common.constant.SystemConstant;
import com.flow.common.exception.BusinessException;
import com.flow.common.exception.ErrorCode;
import com.flow.domain.task.converter.TaskConverter;
import com.flow.domain.task.model.TaskDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class TaskDomainService {

    private final TaskRepository taskRepository;
    private static final TaskConverter TASK_CONVERTER = TaskConverter.INSTANCE;
    private static final Random RANDOM = new Random();

    public TaskDomainService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDO createTask(String apiCode, String apiName, String source, String groupNo,
                             String actionType, Integer priority, Object requestData,
                             TaskReceiptConfigIDTO receiptConfig, Integer maxRetryCount, Long retryIntervalMs) {

        long now = System.currentTimeMillis();
        String taskNo = generateTaskNo();

        TaskRequestContextIDTO requestContext = TaskRequestContextIDTO.builder()
                .customData(requestData)
                .build();

        TaskDO task = TaskDO.builder()
                .taskNo(taskNo)
                .source(source)
                .groupNo(groupNo)
                .apiCode(apiCode)
                .apiName(apiName)
                .actionType(actionType)
                .status("PENDING")
                .interruptFlag(false)
                .compensateStatus("NONE")
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

        TaskIDTO savedDto = taskRepository.save(TASK_CONVERTER.taskDOToSaveTaskParam(task));
        return TASK_CONVERTER.taskIDTOToTaskDO(savedDto);
    }

    public TaskDO cancelTask(String taskNo, String reason, String cancelBy) {
        SelectOneTaskParam param = SelectOneTaskParam.builder().taskNo(taskNo).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        TaskDO task = TASK_CONVERTER.taskIDTOToTaskDO(taskDto);
        if (!task.canCancel()) {
            throw new BusinessException(ErrorCode.TASK_STATUS_NOT_ALLOWED);
        }
        task.cancel(reason, cancelBy);

        TaskIDTO updatedDto = taskRepository.update(TASK_CONVERTER.taskDOToUpdateTaskParam(task));
        return TASK_CONVERTER.taskIDTOToTaskDO(updatedDto);
    }

    public TaskDO retryTask(String taskNo) {
        SelectOneTaskParam param = SelectOneTaskParam.builder().taskNo(taskNo).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        TaskDO task = TASK_CONVERTER.taskIDTOToTaskDO(taskDto);
        if (!task.canRetry()) {
            throw new BusinessException(ErrorCode.TASK_STATUS_NOT_ALLOWED);
        }
        task.prepareRetry();

        TaskIDTO updatedDto = taskRepository.update(TASK_CONVERTER.taskDOToUpdateTaskParam(task));
        return TASK_CONVERTER.taskIDTOToTaskDO(updatedDto);
    }

    public TaskDO startExecute(String taskNo) {
        SelectOneTaskParam param = SelectOneTaskParam.builder().taskNo(taskNo).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        TaskDO task = TASK_CONVERTER.taskIDTOToTaskDO(taskDto);
        task.startExecute();

        TaskIDTO updatedDto = taskRepository.update(TASK_CONVERTER.taskDOToUpdateTaskParam(task));
        return TASK_CONVERTER.taskIDTOToTaskDO(updatedDto);
    }

    public TaskDO completeTask(String taskNo, Object responseData) {
        SelectOneTaskParam param = SelectOneTaskParam.builder().taskNo(taskNo).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        TaskDO task = TASK_CONVERTER.taskIDTOToTaskDO(taskDto);
        task.complete(responseData);

        TaskIDTO updatedDto = taskRepository.update(TASK_CONVERTER.taskDOToUpdateTaskParam(task));
        return TASK_CONVERTER.taskIDTOToTaskDO(updatedDto);
    }

    public TaskDO failTask(String taskNo) {
        SelectOneTaskParam param = SelectOneTaskParam.builder().taskNo(taskNo).build();
        TaskIDTO taskDto = taskRepository.selectOne(param);
        if (taskDto == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        TaskDO task = TASK_CONVERTER.taskIDTOToTaskDO(taskDto);
        task.fail();

        TaskIDTO updatedDto = taskRepository.update(TASK_CONVERTER.taskDOToUpdateTaskParam(task));
        return TASK_CONVERTER.taskIDTOToTaskDO(updatedDto);
    }

    private String generateTaskNo() {
        String timestamp = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", System.currentTimeMillis());
        int random = RANDOM.nextInt(90000) + 10000;
        String randomStr = String.valueOf(random);
        return "TASK" + timestamp + randomStr;
    }

}
