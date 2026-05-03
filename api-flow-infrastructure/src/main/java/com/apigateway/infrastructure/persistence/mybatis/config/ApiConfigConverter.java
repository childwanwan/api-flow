package com.apigateway.infrastructure.persistence.mybatis.config;

import com.apigateway.domain.config.enums.ApiConfigStatus;
import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.infrastructure.persistence.mybatis.config.entity.ApiConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ApiConfigConverter {

    @Named("stringToApiConfigStatus")
    default ApiConfigStatus stringToApiConfigStatus(String value) {
        return value == null ? null : ApiConfigStatus.fromCode(value);
    }

    @Named("apiConfigStatusToString")
    default String apiConfigStatusToString(ApiConfigStatus value) {
        return value == null ? null : value.getCode();
    }

    default ApiConfigEntity toEntity(ApiConfigDO configDO) {
        if (configDO == null) {
            return null;
        }
        return ApiConfigEntity.builder()
                .id(configDO.getId())
                .groupNo(configDO.getGroupNo())
                .apiCode(configDO.getApiCode())
                .apiName(configDO.getApiName())
                .apiDescription(configDO.getApiDescription())
                .status(stringToApiConfigStatus(configDO.getStatus()))
                .requestTimeoutMs(configDO.getRequestTimeoutMs())
                .autoRetryCount(configDO.getAutoRetryCount())
                .retryIntervalMs(configDO.getRetryIntervalMs())
                .rateLimitConfig(configDO.getRateLimitConfig())
                .maxQueueSize(configDO.getMaxQueueSize())
                .filterRules(configDO.getFilterRules())
                .pluginConfig(configDO.getPluginConfig())
                .receiptConfig(configDO.getReceiptConfig())
                .extraConfig(configDO.getExtraConfig())
                .createTimeMs(configDO.getCreateTimeMs())
                .updateTimeMs(configDO.getUpdateTimeMs())
                .createOperator(configDO.getCreateOperator())
                .updateOperator(configDO.getUpdateOperator())
                .deleted(configDO.getDeleted())
                .version(configDO.getVersion())
                .build();
    }

    default ApiConfigDO toDO(ApiConfigEntity configEntity) {
        if (configEntity == null) {
            return null;
        }
        return ApiConfigDO.builder()
                .id(configEntity.getId())
                .groupNo(configEntity.getGroupNo())
                .apiCode(configEntity.getApiCode())
                .apiName(configEntity.getApiName())
                .apiDescription(configEntity.getApiDescription())
                .status(apiConfigStatusToString(configEntity.getStatus()))
                .requestTimeoutMs(configEntity.getRequestTimeoutMs())
                .autoRetryCount(configEntity.getAutoRetryCount())
                .retryIntervalMs(configEntity.getRetryIntervalMs())
                .rateLimitConfig(configEntity.getRateLimitConfig())
                .maxQueueSize(configEntity.getMaxQueueSize())
                .filterRules(configEntity.getFilterRules())
                .pluginConfig(configEntity.getPluginConfig())
                .receiptConfig(configEntity.getReceiptConfig())
                .extraConfig(configEntity.getExtraConfig())
                .createTimeMs(configEntity.getCreateTimeMs())
                .updateTimeMs(configEntity.getUpdateTimeMs())
                .createOperator(configEntity.getCreateOperator())
                .updateOperator(configEntity.getUpdateOperator())
                .deleted(configEntity.getDeleted())
                .version(configEntity.getVersion())
                .build();
    }

}
