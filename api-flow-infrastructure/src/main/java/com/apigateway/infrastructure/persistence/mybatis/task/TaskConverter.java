package com.apigateway.infrastructure.persistence.mybatis.task;

import com.apigateway.common.util.JsonUtil;
import com.apigateway.domain.task.enums.ActionType;
import com.apigateway.domain.task.enums.CompensateStatus;
import com.apigateway.domain.task.enums.TaskStatus;
import com.apigateway.domain.task.model.ExecInfo;
import com.apigateway.domain.task.model.RequestContext;
import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.infrastructure.persistence.mybatis.task.entity.TaskDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface TaskConverter {

    @Mapping(target = "actionType", source = "actionType", qualifiedByName = "stringToActionType")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToTaskStatus")
    @Mapping(target = "compensateStatus", source = "compensateStatus", qualifiedByName = "stringToCompensateStatus")
    @Mapping(target = "requestContext", source = "requestContext", qualifiedByName = "stringToRequestContext")
    @Mapping(target = "execInfo", source = "execInfo", qualifiedByName = "stringToExecInfo")
    @Mapping(target = "responseData", source = "responseData", qualifiedByName = "stringToResponseData")
    TaskEntity toEntity(TaskDO taskDO);

    @Mapping(target = "actionType", source = "actionType", qualifiedByName = "actionTypeToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "taskStatusToString")
    @Mapping(target = "compensateStatus", source = "compensateStatus", qualifiedByName = "compensateStatusToString")
    @Mapping(target = "requestContext", source = "requestContext", qualifiedByName = "requestContextToString")
    @Mapping(target = "execInfo", source = "execInfo", qualifiedByName = "execInfoToString")
    @Mapping(target = "responseData", source = "responseData", qualifiedByName = "responseDataToString")
    TaskDO toDO(TaskEntity taskEntity);

    @Named("stringToActionType")
    default ActionType stringToActionType(String value) {
        return value == null ? null : ActionType.fromCode(value);
    }

    @Named("actionTypeToString")
    default String actionTypeToString(ActionType value) {
        return value == null ? null : value.getCode();
    }

    @Named("stringToTaskStatus")
    default TaskStatus stringToTaskStatus(String value) {
        return value == null ? null : TaskStatus.fromCode(value);
    }

    @Named("taskStatusToString")
    default String taskStatusToString(TaskStatus value) {
        return value == null ? null : value.getCode();
    }

    @Named("stringToCompensateStatus")
    default CompensateStatus stringToCompensateStatus(String value) {
        return value == null ? null : CompensateStatus.fromCode(value);
    }

    @Named("compensateStatusToString")
    default String compensateStatusToString(CompensateStatus value) {
        return value == null ? null : value.getCode();
    }

    @Named("stringToRequestContext")
    default RequestContext stringToRequestContext(String value) {
        return value == null ? null : JsonUtil.toObject(value, RequestContext.class);
    }

    @Named("requestContextToString")
    default String requestContextToString(RequestContext value) {
        return value == null ? null : JsonUtil.toJson(value);
    }

    @Named("stringToExecInfo")
    default ExecInfo stringToExecInfo(String value) {
        return value == null ? null : JsonUtil.toObject(value, ExecInfo.class);
    }

    @Named("execInfoToString")
    default String execInfoToString(ExecInfo value) {
        return value == null ? null : JsonUtil.toJson(value);
    }

    @Named("stringToResponseData")
    default Object stringToResponseData(String value) {
        return value == null ? null : JsonUtil.toObject(value, Object.class);
    }

    @Named("responseDataToString")
    default String responseDataToString(Object value) {
        return value == null ? null : JsonUtil.toJson(value);
    }

    @Named("stringToMap")
    default Map<String, Object> stringToMap(String value) {
        return value == null ? null : JsonUtil.toObject(value, Map.class);
    }

    @Named("mapToString")
    default String mapToString(Map<String, Object> value) {
        return value == null ? null : JsonUtil.toJson(value);
    }

}
