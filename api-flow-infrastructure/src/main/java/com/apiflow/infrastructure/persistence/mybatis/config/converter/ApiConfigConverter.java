package com.apiflow.infrastructure.persistence.mybatis.config.converter;

import com.apiflow.api.repository.config.idto.*;
import com.apiflow.api.repository.config.param.*;
import com.apiflow.api.repository.task.idto.TaskReceiptConfigIDTO;
import com.apiflow.api.repository.task.param.SaveTaskReceiptConfigParam;
import com.apiflow.api.repository.task.param.UpdateTaskReceiptConfigParam;
import com.apiflow.common.result.PageResult;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.infrastructure.persistence.mybatis.config.entity.ApiConfigPO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper
public interface ApiConfigConverter {
    ApiConfigConverter INSTANCE = Mappers.getMapper(ApiConfigConverter.class);

    @Named("saveApiConfigParamToApiConfigEntityPO")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rateLimitConfig", qualifiedByName = "saveApiConfigRateLimitConfigParamToJson")
    @Mapping(target = "filterRules", qualifiedByName = "saveApiConfigFilterRulesParamToJson")
    @Mapping(target = "pluginConfig", qualifiedByName = "saveApiConfigPluginConfigParamToJson")
    @Mapping(target = "receiptConfig", qualifiedByName = "saveTaskReceiptConfigParamToJson")
    @Mapping(target = "extraConfig", qualifiedByName = "saveApiConfigExtraConfigParamToJson")
    ApiConfigPO saveApiConfigParamToApiConfigEntityPO(SaveApiConfigParam param);

    @Named("updateApiConfigParamToApiConfigEntityPO")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "updateApiConfigRateLimitConfigParamToJson")
    @Mapping(target = "filterRules", qualifiedByName = "updateApiConfigFilterRulesParamToJson")
    @Mapping(target = "pluginConfig", qualifiedByName = "updateApiConfigPluginConfigParamToJson")
    @Mapping(target = "receiptConfig", qualifiedByName = "updateTaskReceiptConfigParamToJson")
    @Mapping(target = "extraConfig", qualifiedByName = "updateApiConfigExtraConfigParamToJson")
    ApiConfigPO updateApiConfigParamToApiConfigEntityPO(UpdateApiConfigParam param);

    @Named("saveApiConfigRateLimitConfigParamToJson")
    default String saveApiConfigRateLimitConfigParamToJson(SaveApiConfigRateLimitConfigParam rateLimitConfig) {
        return rateLimitConfig == null ? null : JsonUtil.toJson(rateLimitConfig);
    }

    @Named("saveApiConfigFilterRulesParamToJson")
    default String saveApiConfigFilterRulesParamToJson(SaveApiConfigFilterRulesParam filterRules) {
        return filterRules == null ? null : JsonUtil.toJson(filterRules);
    }

    @Named("saveApiConfigPluginConfigParamToJson")
    default String saveApiConfigPluginConfigParamToJson(SaveApiConfigPluginConfigParam pluginConfig) {
        return pluginConfig == null ? null : JsonUtil.toJson(pluginConfig);
    }

    @Named("saveTaskReceiptConfigParamToJson")
    default String saveTaskReceiptConfigParamToJson(SaveTaskReceiptConfigParam receiptConfig) {
        return receiptConfig == null ? null : JsonUtil.toJson(receiptConfig);
    }

    @Named("saveApiConfigExtraConfigParamToJson")
    default String saveApiConfigExtraConfigParamToJson(SaveApiConfigExtraConfigParam extraConfig) {
        return extraConfig == null ? null : JsonUtil.toJson(extraConfig);
    }

    @Named("updateApiConfigRateLimitConfigParamToJson")
    default String updateApiConfigRateLimitConfigParamToJson(UpdateApiConfigRateLimitConfigParam rateLimitConfig) {
        return rateLimitConfig == null ? null : JsonUtil.toJson(rateLimitConfig);
    }

    @Named("updateApiConfigFilterRulesParamToJson")
    default String updateApiConfigFilterRulesParamToJson(UpdateApiConfigFilterRulesParam filterRules) {
        return filterRules == null ? null : JsonUtil.toJson(filterRules);
    }

    @Named("updateApiConfigPluginConfigParamToJson")
    default String updateApiConfigPluginConfigParamToJson(UpdateApiConfigPluginConfigParam pluginConfig) {
        return pluginConfig == null ? null : JsonUtil.toJson(pluginConfig);
    }

    @Named("updateTaskReceiptConfigParamToJson")
    default String updateTaskReceiptConfigParamToJson(UpdateTaskReceiptConfigParam receiptConfig) {
        return receiptConfig == null ? null : JsonUtil.toJson(receiptConfig);
    }

    @Named("updateApiConfigExtraConfigParamToJson")
    default String updateApiConfigExtraConfigParamToJson(UpdateApiConfigExtraConfigParam extraConfig) {
        return extraConfig == null ? null : JsonUtil.toJson(extraConfig);
    }

    @Named("jsonToApiConfigRateLimitConfigIDTO")
    default ApiConfigRateLimitConfigIDTO jsonToApiConfigRateLimitConfigIDTO(String json) {
        return json == null ? null : JsonUtil.toObject(json, ApiConfigRateLimitConfigIDTO.class);
    }

    @Named("jsonToApiConfigFilterRulesIDTO")
    default ApiConfigFilterRulesIDTO jsonToApiConfigFilterRulesIDTO(String json) {
        return json == null ? null : JsonUtil.toObject(json, ApiConfigFilterRulesIDTO.class);
    }

    @Named("jsonToApiConfigPluginConfigIDTO")
    default ApiConfigPluginConfigIDTO jsonToApiConfigPluginConfigIDTO(String json) {
        return json == null ? null : JsonUtil.toObject(json, ApiConfigPluginConfigIDTO.class);
    }

    @Named("jsonToTaskReceiptConfigIDTO")
    default TaskReceiptConfigIDTO jsonToTaskReceiptConfigIDTO(String json) {
        return json == null ? null : JsonUtil.toObject(json, TaskReceiptConfigIDTO.class);
    }

    @Named("jsonToApiConfigExtraConfigIDTO")
    default ApiConfigExtraConfigIDTO jsonToApiConfigExtraConfigIDTO(String json) {
        return json == null ? null : JsonUtil.toObject(json, ApiConfigExtraConfigIDTO.class);
    }

    @Named("apiConfigEntityPOToApiConfigIDTO")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "jsonToApiConfigRateLimitConfigIDTO")
    @Mapping(target = "filterRules", qualifiedByName = "jsonToApiConfigFilterRulesIDTO")
    @Mapping(target = "pluginConfig", qualifiedByName = "jsonToApiConfigPluginConfigIDTO")
    @Mapping(target = "receiptConfig", qualifiedByName = "jsonToTaskReceiptConfigIDTO")
    @Mapping(target = "extraConfig", qualifiedByName = "jsonToApiConfigExtraConfigIDTO")
    ApiConfigIDTO apiConfigEntityPOToApiConfigIDTO(ApiConfigPO configPO);

    default PageResult<ApiConfigIDTO> apiConfigPOIPage2PageResult(IPage<ApiConfigPO> result) {
        if (result == null) {
            return null;
        }
        return PageResult.<ApiConfigIDTO>builder()
                .records(result.getRecords().stream()
                        .map(this::apiConfigEntityPOToApiConfigIDTO)
                        .collect(Collectors.toList()))
                .total(result.getTotal())
                .size(result.getSize())
                .current(result.getCurrent())
                .pages(result.getPages())
                .build();
    }

}
