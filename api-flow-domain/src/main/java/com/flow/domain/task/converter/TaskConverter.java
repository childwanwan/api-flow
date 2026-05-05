package com.flow.domain.task.converter;

import com.flow.api.repository.task.idto.*;
import com.flow.api.repository.task.param.*;
import com.flow.domain.task.model.TaskDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(implementationName = "DomainTaskConverter")
public interface TaskConverter {

    TaskConverter INSTANCE = Mappers.getMapper(TaskConverter.class);

    @Named("taskDOToTaskIDTO")
    TaskIDTO taskDOToTaskIDTO(TaskDO taskDO);

    @Named("taskIDTOToTaskDO")
    TaskDO taskIDTOToTaskDO(TaskIDTO taskIDTO);

    @Named("taskDOToSaveTaskParam")
    @Mapping(target = "requestContext", qualifiedByName = "taskRequestContextIDTOToSaveTaskRequestContextParam")
    @Mapping(target = "execInfo", qualifiedByName = "taskExecInfoIDTOToSaveTaskExecInfoParam")
    @Mapping(target = "receiptConfig", qualifiedByName = "taskReceiptConfigIDTOToSaveTaskReceiptConfigParam")
    @Mapping(target = "receiptInfo", qualifiedByName = "taskReceiptInfoIDTOToSaveTaskReceiptInfoParam")
    SaveTaskParam taskDOToSaveTaskParam(TaskDO taskDO);

    @Named("taskDOToUpdateTaskParam")
    @Mapping(target = "requestContext", qualifiedByName = "taskRequestContextIDTOToUpdateTaskRequestContextParam")
    @Mapping(target = "execInfo", qualifiedByName = "taskExecInfoIDTOToUpdateTaskExecInfoParam")
    @Mapping(target = "receiptConfig", qualifiedByName = "taskReceiptConfigIDTOToUpdateTaskReceiptConfigParam")
    @Mapping(target = "receiptInfo", qualifiedByName = "taskReceiptInfoIDTOToUpdateTaskReceiptInfoParam")
    UpdateTaskParam taskDOToUpdateTaskParam(TaskDO taskDO);

    @Named("taskRequestContextIDTOToSaveTaskRequestContextParam")
    @Mapping(target = "params", source = "params")
    @Mapping(target = "customData", source = "customData")
    SaveTaskRequestContextParam taskRequestContextIDTOToSaveTaskRequestContextParam(TaskRequestContextIDTO dto);

    @Named("taskExecInfoIDTOToSaveTaskExecInfoParam")
    @Mapping(target = "steps", source = "steps")
    SaveTaskExecInfoParam taskExecInfoIDTOToSaveTaskExecInfoParam(TaskExecInfoIDTO dto);

    @Named("taskPluginStepIDTOToSaveTaskPluginStepParam")
    SaveTaskPluginStepParam taskPluginStepIDTOToSaveTaskPluginStepParam(TaskPluginStepIDTO dto);

    @Named("taskReceiptConfigIDTOToSaveTaskReceiptConfigParam")
    @Mapping(target = "http", source = "http")
    @Mapping(target = "mq", source = "mq")
    SaveTaskReceiptConfigParam taskReceiptConfigIDTOToSaveTaskReceiptConfigParam(TaskReceiptConfigIDTO dto);

    @Named("taskHttpReceiptIDTOToSaveTaskHttpReceiptParam")
    @Mapping(target = "retryPolicy", source = "retryPolicy")
    SaveTaskHttpReceiptParam taskHttpReceiptIDTOToSaveTaskHttpReceiptParam(TaskHttpReceiptIDTO dto);

    @Named("taskReceiptRetryPolicyIDTOToSaveTaskReceiptRetryPolicyParam")
    SaveTaskReceiptRetryPolicyParam taskReceiptRetryPolicyIDTOToSaveTaskReceiptRetryPolicyParam(TaskReceiptRetryPolicyIDTO dto);

    @Named("taskMqReceiptIDTOToSaveTaskMqReceiptParam")
    SaveTaskMqReceiptParam taskMqReceiptIDTOToSaveTaskMqReceiptParam(TaskMqReceiptIDTO dto);

    @Named("taskReceiptInfoIDTOToSaveTaskReceiptInfoParam")
    @Mapping(target = "http", source = "http")
    @Mapping(target = "mq", source = "mq")
    SaveTaskReceiptInfoParam taskReceiptInfoIDTOToSaveTaskReceiptInfoParam(TaskReceiptInfoIDTO dto);

    @Named("taskHttpReceiptRecordIDTOToSaveTaskHttpReceiptRecordParam")
    @Mapping(target = "attempts", source = "attempts")
    SaveTaskHttpReceiptRecordParam taskHttpReceiptRecordIDTOToSaveTaskHttpReceiptRecordParam(TaskHttpReceiptRecordIDTO dto);

    @Named("taskMqReceiptRecordIDTOToSaveTaskMqReceiptRecordParam")
    @Mapping(target = "attempts", source = "attempts")
    SaveTaskMqReceiptRecordParam taskMqReceiptRecordIDTOToSaveTaskMqReceiptRecordParam(TaskMqReceiptRecordIDTO dto);

    @Named("taskReceiptAttemptIDTOToSaveTaskReceiptAttemptParam")
    SaveTaskReceiptAttemptParam taskReceiptAttemptIDTOToSaveTaskReceiptAttemptParam(TaskReceiptAttemptIDTO dto);

    @Named("taskRequestContextIDTOToUpdateTaskRequestContextParam")
    @Mapping(target = "params", source = "params")
    @Mapping(target = "customData", source = "customData")
    UpdateTaskRequestContextParam taskRequestContextIDTOToUpdateTaskRequestContextParam(TaskRequestContextIDTO dto);

    @Named("taskExecInfoIDTOToUpdateTaskExecInfoParam")
    @Mapping(target = "steps", source = "steps")
    UpdateTaskExecInfoParam taskExecInfoIDTOToUpdateTaskExecInfoParam(TaskExecInfoIDTO dto);

    @Named("taskPluginStepIDTOToUpdateTaskPluginStepParam")
    UpdateTaskPluginStepParam taskPluginStepIDTOToUpdateTaskPluginStepParam(TaskPluginStepIDTO dto);

    @Named("taskReceiptConfigIDTOToUpdateTaskReceiptConfigParam")
    @Mapping(target = "http", source = "http")
    @Mapping(target = "mq", source = "mq")
    UpdateTaskReceiptConfigParam taskReceiptConfigIDTOToUpdateTaskReceiptConfigParam(TaskReceiptConfigIDTO dto);

    @Named("taskHttpReceiptIDTOToUpdateTaskHttpReceiptParam")
    @Mapping(target = "retryPolicy", source = "retryPolicy")
    UpdateTaskHttpReceiptParam taskHttpReceiptIDTOToUpdateTaskHttpReceiptParam(TaskHttpReceiptIDTO dto);

    @Named("taskReceiptRetryPolicyIDTOToUpdateTaskReceiptRetryPolicyParam")
    UpdateTaskReceiptRetryPolicyParam taskReceiptRetryPolicyIDTOToUpdateTaskReceiptRetryPolicyParam(TaskReceiptRetryPolicyIDTO dto);

    @Named("taskMqReceiptIDTOToUpdateTaskMqReceiptParam")
    UpdateTaskMqReceiptParam taskMqReceiptIDTOToUpdateTaskMqReceiptParam(TaskMqReceiptIDTO dto);

    @Named("taskReceiptInfoIDTOToUpdateTaskReceiptInfoParam")
    @Mapping(target = "http", source = "http")
    @Mapping(target = "mq", source = "mq")
    UpdateTaskReceiptInfoParam taskReceiptInfoIDTOToUpdateTaskReceiptInfoParam(TaskReceiptInfoIDTO dto);

    @Named("taskHttpReceiptRecordIDTOToUpdateTaskHttpReceiptRecordParam")
    @Mapping(target = "attempts", source = "attempts")
    UpdateTaskHttpReceiptRecordParam taskHttpReceiptRecordIDTOToUpdateTaskHttpReceiptRecordParam(TaskHttpReceiptRecordIDTO dto);

    @Named("taskMqReceiptRecordIDTOToUpdateTaskMqReceiptRecordParam")
    @Mapping(target = "attempts", source = "attempts")
    UpdateTaskMqReceiptRecordParam taskMqReceiptRecordIDTOToUpdateTaskMqReceiptRecordParam(TaskMqReceiptRecordIDTO dto);

    @Named("taskReceiptAttemptIDTOToUpdateTaskReceiptAttemptParam")
    UpdateTaskReceiptAttemptParam taskReceiptAttemptIDTOToUpdateTaskReceiptAttemptParam(TaskReceiptAttemptIDTO dto);

}
