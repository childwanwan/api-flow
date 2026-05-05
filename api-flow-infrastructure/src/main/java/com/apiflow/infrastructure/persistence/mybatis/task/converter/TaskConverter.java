package com.apiflow.infrastructure.persistence.mybatis.task.converter;

import com.apiflow.api.repository.task.idto.*;
import com.apiflow.api.repository.task.param.*;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.infrastructure.persistence.mybatis.task.entity.TaskPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskConverter {

    TaskConverter INSTANCE = Mappers.getMapper(TaskConverter.class);


    @Named("saveTaskParamToTaskEntityPO")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestContext", qualifiedByName = "saveTaskRequestContextParamToJson")
    @Mapping(target = "execInfo", qualifiedByName = "saveTaskExecInfoParamToJson")
    @Mapping(target = "responseData", qualifiedByName = "objectToJson")
    @Mapping(target = "receiptConfig", qualifiedByName = "saveTaskReceiptConfigParamToJson")
    @Mapping(target = "receiptInfo", qualifiedByName = "saveTaskReceiptInfoParamToJson")
    TaskPO saveTaskParamToTaskEntityPO(SaveTaskParam param);

    @Named("updateTaskParamToTaskEntityPO")
    @Mapping(target = "requestContext", qualifiedByName = "updateTaskRequestContextParamToJson")
    @Mapping(target = "execInfo", qualifiedByName = "updateTaskExecInfoParamToJson")
    @Mapping(target = "responseData", qualifiedByName = "objectToJson")
    @Mapping(target = "receiptConfig", qualifiedByName = "updateTaskReceiptConfigParamToJson")
    @Mapping(target = "receiptInfo", qualifiedByName = "updateTaskReceiptInfoParamToJson")
    TaskPO updateTaskParamToTaskEntityPO(UpdateTaskParam param);

    @Named("saveTaskRequestContextParamToJson")
    default String saveTaskRequestContextParamToJson(SaveTaskRequestContextParam requestContext) {
        return requestContext == null ? null : JsonUtil.toJson(requestContext);
    }

    @Named("saveTaskExecInfoParamToJson")
    default String saveTaskExecInfoParamToJson(SaveTaskExecInfoParam execInfo) {
        return execInfo == null ? null : JsonUtil.toJson(execInfo);
    }

    @Named("saveTaskReceiptConfigParamToJson")
    default String saveTaskReceiptConfigParamToJson(SaveTaskReceiptConfigParam receiptConfig) {
        return receiptConfig == null ? null : JsonUtil.toJson(receiptConfig);
    }

    @Named("saveTaskReceiptInfoParamToJson")
    default String saveTaskReceiptInfoParamToJson(SaveTaskReceiptInfoParam receiptInfo) {
        return receiptInfo == null ? null : JsonUtil.toJson(receiptInfo);
    }

    @Named("updateTaskRequestContextParamToJson")
    default String updateTaskRequestContextParamToJson(UpdateTaskRequestContextParam requestContext) {
        return requestContext == null ? null : JsonUtil.toJson(requestContext);
    }

    @Named("updateTaskExecInfoParamToJson")
    default String updateTaskExecInfoParamToJson(UpdateTaskExecInfoParam execInfo) {
        return execInfo == null ? null : JsonUtil.toJson(execInfo);
    }

    @Named("updateTaskReceiptConfigParamToJson")
    default String updateTaskReceiptConfigParamToJson(UpdateTaskReceiptConfigParam receiptConfig) {
        return receiptConfig == null ? null : JsonUtil.toJson(receiptConfig);
    }

    @Named("updateTaskReceiptInfoParamToJson")
    default String updateTaskReceiptInfoParamToJson(UpdateTaskReceiptInfoParam receiptInfo) {
        return receiptInfo == null ? null : JsonUtil.toJson(receiptInfo);
    }

    @Named("objectToJson")
    default String objectToJson(Object responseData) {
        return responseData == null ? null : JsonUtil.toJson(responseData);
    }

    @Named("jsonToTaskRequestContextIDTO")
    default TaskRequestContextIDTO jsonToTaskRequestContextIDTO(String json) {
        return json == null ? null : JsonUtil.toObject(json, TaskRequestContextIDTO.class);
    }

    @Named("jsonToTaskExecInfoIDTO")
    default TaskExecInfoIDTO jsonToTaskExecInfoIDTO(String json) {
        return json == null ? null : JsonUtil.toObject(json, TaskExecInfoIDTO.class);
    }

    @Named("jsonToObject")
    default Object jsonToObject(String json) {
        return json == null ? null : JsonUtil.toObject(json, tools.jackson.databind.JsonNode.class);
    }

    @Named("jsonToTaskReceiptConfigIDTO")
    default TaskReceiptConfigIDTO jsonToTaskReceiptConfigIDTO(String json) {
        return json == null ? null : JsonUtil.toObject(json, TaskReceiptConfigIDTO.class);
    }

    @Named("jsonToTaskReceiptInfoIDTO")
    default TaskReceiptInfoIDTO jsonToTaskReceiptInfoIDTO(String json) {
        return json == null ? null : JsonUtil.toObject(json, TaskReceiptInfoIDTO.class);
    }

    @Named("taskEntityPOToTaskIDTO")
    @Mapping(target = "requestContext", qualifiedByName = "jsonToTaskRequestContextIDTO")
    @Mapping(target = "execInfo", qualifiedByName = "jsonToTaskExecInfoIDTO")
    @Mapping(target = "responseData", qualifiedByName = "jsonToObject")
    @Mapping(target = "receiptConfig", qualifiedByName = "jsonToTaskReceiptConfigIDTO")
    @Mapping(target = "receiptInfo", qualifiedByName = "jsonToTaskReceiptInfoIDTO")
    TaskIDTO taskEntityPOToTaskIDTO(TaskPO taskPO);

}
