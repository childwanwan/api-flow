package com.apiflow.domain.task.converter;

import com.apiflow.api.repository.task.idto.*;
import com.apiflow.api.repository.task.param.*;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.domain.task.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(implementationName = "DomainTaskConverter")
public interface TaskConverter {

    TaskConverter INSTANCE = Mappers.getMapper(TaskConverter.class);

    @Named("taskIDTOToTask")
    @Mapping(target = "requestContext", qualifiedByName = "requestContextIDTOToDomain")
    @Mapping(target = "execInfo", qualifiedByName = "execInfoIDTOToDomain")
    @Mapping(target = "receiptConfig", qualifiedByName = "receiptConfigIDTOToDomain")
    @Mapping(target = "receiptInfo", qualifiedByName = "receiptInfoIDTOToDomain")
    Task taskIDTOToTask(TaskIDTO taskIDTO);

    @Named("taskToSaveTaskParam")
    @Mapping(target = "requestContext", qualifiedByName = "requestContextToSaveParam")
    @Mapping(target = "execInfo", qualifiedByName = "execInfoToSaveParam")
    @Mapping(target = "receiptConfig", qualifiedByName = "receiptConfigToSaveParam")
    @Mapping(target = "receiptInfo", qualifiedByName = "receiptInfoToSaveParam")
    SaveTaskParam taskToSaveTaskParam(Task task);

    @Named("taskToUpdateTaskParam")
    @Mapping(target = "requestContext", qualifiedByName = "requestContextToUpdateParam")
    @Mapping(target = "execInfo", qualifiedByName = "execInfoToUpdateParam")
    @Mapping(target = "receiptConfig", qualifiedByName = "receiptConfigToUpdateParam")
    @Mapping(target = "receiptInfo", qualifiedByName = "receiptInfoToUpdateParam")
    UpdateTaskParam taskToUpdateTaskParam(Task task);

    @Named("requestContextIDTOToDomain")
    RequestContext requestContextIDTOToDomain(TaskRequestContextIDTO dto);

    @Named("execInfoIDTOToDomain")
    ExecInfo execInfoIDTOToDomain(TaskExecInfoIDTO dto);

    @Named("pluginStepIDTOToDomain")
    PluginStep pluginStepIDTOToDomain(TaskPluginStepIDTO dto);

    @Named("receiptConfigIDTOToDomain")
    ReceiptConfig receiptConfigIDTOToDomain(TaskReceiptConfigIDTO dto);

    @Named("httpReceiptIDTOToDomain")
    HttpReceipt httpReceiptIDTOToDomain(TaskHttpReceiptIDTO dto);

    @Named("mqReceiptIDTOToDomain")
    MqReceipt mqReceiptIDTOToDomain(TaskMqReceiptIDTO dto);

    @Named("retryPolicyIDTOToDomain")
    ReceiptRetryPolicy retryPolicyIDTOToDomain(TaskReceiptRetryPolicyIDTO dto);

    @Named("receiptInfoIDTOToDomain")
    ReceiptInfo receiptInfoIDTOToDomain(TaskReceiptInfoIDTO dto);

    @Named("httpReceiptRecordIDTOToDomain")
    HttpReceiptRecord httpReceiptRecordIDTOToDomain(TaskHttpReceiptRecordIDTO dto);

    @Named("mqReceiptRecordIDTOToDomain")
    MqReceiptRecord mqReceiptRecordIDTOToDomain(TaskMqReceiptRecordIDTO dto);

    @Named("receiptAttemptIDTOToDomain")
    ReceiptAttempt receiptAttemptIDTOToDomain(TaskReceiptAttemptIDTO dto);

    @Named("requestContextToSaveParam")
    SaveTaskRequestContextParam requestContextToSaveParam(RequestContext context);

    @Named("execInfoToSaveParam")
    SaveTaskExecInfoParam execInfoToSaveParam(ExecInfo info);

    @Named("pluginStepToSaveParam")
    SaveTaskPluginStepParam pluginStepToSaveParam(PluginStep step);

    @Named("receiptConfigToSaveParam")
    SaveTaskReceiptConfigParam receiptConfigToSaveParam(ReceiptConfig config);

    @Named("httpReceiptToSaveParam")
    SaveTaskHttpReceiptParam httpReceiptToSaveParam(HttpReceipt receipt);

    @Named("retryPolicyToSaveParam")
    SaveTaskReceiptRetryPolicyParam retryPolicyToSaveParam(ReceiptRetryPolicy policy);

    @Named("mqReceiptToSaveParam")
    SaveTaskMqReceiptParam mqReceiptToSaveParam(MqReceipt receipt);

    @Named("receiptInfoToSaveParam")
    SaveTaskReceiptInfoParam receiptInfoToSaveParam(ReceiptInfo info);

    @Named("httpReceiptRecordToSaveParam")
    SaveTaskHttpReceiptRecordParam httpReceiptRecordToSaveParam(HttpReceiptRecord record);

    @Named("mqReceiptRecordToSaveParam")
    SaveTaskMqReceiptRecordParam mqReceiptRecordToSaveParam(MqReceiptRecord record);

    @Named("receiptAttemptToSaveParam")
    SaveTaskReceiptAttemptParam receiptAttemptToSaveParam(ReceiptAttempt attempt);

    @Named("requestContextToUpdateParam")
    UpdateTaskRequestContextParam requestContextToUpdateParam(RequestContext context);

    @Named("execInfoToUpdateParam")
    UpdateTaskExecInfoParam execInfoToUpdateParam(ExecInfo info);

    @Named("pluginStepToUpdateParam")
    UpdateTaskPluginStepParam pluginStepToUpdateParam(PluginStep step);

    @Named("receiptConfigToUpdateParam")
    UpdateTaskReceiptConfigParam receiptConfigToUpdateParam(ReceiptConfig config);

    @Named("httpReceiptToUpdateParam")
    UpdateTaskHttpReceiptParam httpReceiptToUpdateParam(HttpReceipt receipt);

    @Named("retryPolicyToUpdateParam")
    UpdateTaskReceiptRetryPolicyParam retryPolicyToUpdateParam(ReceiptRetryPolicy policy);

    @Named("mqReceiptToUpdateParam")
    UpdateTaskMqReceiptParam mqReceiptToUpdateParam(MqReceipt receipt);

    @Named("receiptInfoToUpdateParam")
    UpdateTaskReceiptInfoParam receiptInfoToUpdateParam(ReceiptInfo info);

    @Named("httpReceiptRecordToUpdateParam")
    UpdateTaskHttpReceiptRecordParam httpReceiptRecordToUpdateParam(HttpReceiptRecord record);

    @Named("mqReceiptRecordToUpdateParam")
    UpdateTaskMqReceiptRecordParam mqReceiptRecordToUpdateParam(MqReceiptRecord record);

    @Named("receiptAttemptToUpdateParam")
    UpdateTaskReceiptAttemptParam receiptAttemptToUpdateParam(ReceiptAttempt attempt);

    default String map(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return JsonUtil.toJson(value);
    }
}
