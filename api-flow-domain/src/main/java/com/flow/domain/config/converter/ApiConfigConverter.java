package com.flow.domain.config.converter;

import com.flow.api.repository.config.dto.*;
import com.flow.api.repository.config.param.*;
import com.flow.api.repository.task.idto.TaskReceiptConfigIDTO;
import com.flow.api.repository.task.param.SaveTaskReceiptConfigParam;
import com.flow.api.repository.task.param.UpdateTaskReceiptConfigParam;
import com.flow.domain.config.model.ApiConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(implementationName = "DomainApiConfigConverter")
public interface ApiConfigConverter {

    ApiConfigConverter INSTANCE = Mappers.getMapper(ApiConfigConverter.class);

    @Named("apiConfigDOToApiConfigIDTO")
    ApiConfigIDTO apiConfigDOToApiConfigIDTO(ApiConfigDO configDO);

    @Named("apiConfigIDTOToApiConfigDO")
    ApiConfigDO apiConfigIDTOToApiConfigDO(ApiConfigIDTO dto);

    @Named("apiConfigDOToSaveApiConfigParam")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "apiConfigRateLimitConfigIDTOToSaveApiConfigRateLimitConfigParam")
    @Mapping(target = "filterRules", qualifiedByName = "apiConfigFilterRulesIDTOToSaveApiConfigFilterRulesParam")
    @Mapping(target = "pluginConfig", qualifiedByName = "apiConfigPluginConfigIDTOToSaveApiConfigPluginConfigParam")
    @Mapping(target = "receiptConfig", qualifiedByName = "taskReceiptConfigIDTOToSaveTaskReceiptConfigParam")
    @Mapping(target = "extraConfig", qualifiedByName = "apiConfigExtraConfigIDTOToSaveApiConfigExtraConfigParam")
    SaveApiConfigParam apiConfigDOToSaveApiConfigParam(ApiConfigDO configDO);

    @Named("apiConfigDOToUpdateApiConfigParam")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "apiConfigRateLimitConfigIDTOToUpdateApiConfigRateLimitConfigParam")
    @Mapping(target = "filterRules", qualifiedByName = "apiConfigFilterRulesIDTOToUpdateApiConfigFilterRulesParam")
    @Mapping(target = "pluginConfig", qualifiedByName = "apiConfigPluginConfigIDTOToUpdateApiConfigPluginConfigParam")
    @Mapping(target = "receiptConfig", qualifiedByName = "taskReceiptConfigIDTOToUpdateTaskReceiptConfigParam")
    @Mapping(target = "extraConfig", qualifiedByName = "apiConfigExtraConfigIDTOToUpdateApiConfigExtraConfigParam")
    UpdateApiConfigParam apiConfigDOToUpdateApiConfigParam(ApiConfigDO configDO);

    @Named("saveApiConfigParamToApiConfigDO")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rateLimitConfig", qualifiedByName = "saveApiConfigRateLimitConfigParamToApiConfigRateLimitConfigIDTO")
    @Mapping(target = "filterRules", qualifiedByName = "saveApiConfigFilterRulesParamToApiConfigFilterRulesIDTO")
    @Mapping(target = "pluginConfig", qualifiedByName = "saveApiConfigPluginConfigParamToApiConfigPluginConfigIDTO")
    @Mapping(target = "receiptConfig", qualifiedByName = "saveTaskReceiptConfigParamToTaskReceiptConfigIDTO")
    @Mapping(target = "extraConfig", qualifiedByName = "saveApiConfigExtraConfigParamToApiConfigExtraConfigIDTO")
    ApiConfigDO saveApiConfigParamToApiConfigDO(SaveApiConfigParam param);

    @Named("updateApiConfigParamToApiConfigDO")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "updateApiConfigRateLimitConfigParamToApiConfigRateLimitConfigIDTO")
    @Mapping(target = "filterRules", qualifiedByName = "updateApiConfigFilterRulesParamToApiConfigFilterRulesIDTO")
    @Mapping(target = "pluginConfig", qualifiedByName = "updateApiConfigPluginConfigParamToApiConfigPluginConfigIDTO")
    @Mapping(target = "receiptConfig", qualifiedByName = "updateTaskReceiptConfigParamToTaskReceiptConfigIDTO")
    @Mapping(target = "extraConfig", qualifiedByName = "updateApiConfigExtraConfigParamToApiConfigExtraConfigIDTO")
    ApiConfigDO updateApiConfigParamToApiConfigDO(UpdateApiConfigParam param);

    @Named("apiConfigRateLimitConfigIDTOToSaveApiConfigRateLimitConfigParam")
    SaveApiConfigRateLimitConfigParam apiConfigRateLimitConfigIDTOToSaveApiConfigRateLimitConfigParam(ApiConfigRateLimitConfigIDTO dto);

    @Named("apiConfigRateLimitRuleIDTOToSaveApiConfigRateLimitRuleParam")
    SaveApiConfigRateLimitRuleParam apiConfigRateLimitRuleIDTOToSaveApiConfigRateLimitRuleParam(ApiConfigRateLimitRuleIDTO dto);

    @Named("apiConfigFilterRulesIDTOToSaveApiConfigFilterRulesParam")
    SaveApiConfigFilterRulesParam apiConfigFilterRulesIDTOToSaveApiConfigFilterRulesParam(ApiConfigFilterRulesIDTO dto);

    @Named("apiConfigFilterRuleIDTOToSaveApiConfigFilterRuleParam")
    SaveApiConfigFilterRuleParam apiConfigFilterRuleIDTOToSaveApiConfigFilterRuleParam(ApiConfigFilterRuleIDTO dto);

    @Named("apiConfigPluginConfigIDTOToSaveApiConfigPluginConfigParam")
    SaveApiConfigPluginConfigParam apiConfigPluginConfigIDTOToSaveApiConfigPluginConfigParam(ApiConfigPluginConfigIDTO dto);

    @Named("apiConfigPluginChainItemIDTOToSaveApiConfigPluginChainItemParam")
    SaveApiConfigPluginChainItemParam apiConfigPluginChainItemIDTOToSaveApiConfigPluginChainItemParam(ApiConfigPluginChainItemIDTO dto);

    @Named("taskReceiptConfigIDTOToSaveTaskReceiptConfigParam")
    SaveTaskReceiptConfigParam taskReceiptConfigIDTOToSaveTaskReceiptConfigParam(TaskReceiptConfigIDTO dto);

    @Named("apiConfigExtraConfigIDTOToSaveApiConfigExtraConfigParam")
    SaveApiConfigExtraConfigParam apiConfigExtraConfigIDTOToSaveApiConfigExtraConfigParam(ApiConfigExtraConfigIDTO dto);

    @Named("apiConfigRateLimitConfigIDTOToUpdateApiConfigRateLimitConfigParam")
    UpdateApiConfigRateLimitConfigParam apiConfigRateLimitConfigIDTOToUpdateApiConfigRateLimitConfigParam(ApiConfigRateLimitConfigIDTO dto);

    @Named("apiConfigRateLimitRuleIDTOToUpdateApiConfigRateLimitRuleParam")
    UpdateApiConfigRateLimitRuleParam apiConfigRateLimitRuleIDTOToUpdateApiConfigRateLimitRuleParam(ApiConfigRateLimitRuleIDTO dto);

    @Named("apiConfigFilterRulesIDTOToUpdateApiConfigFilterRulesParam")
    UpdateApiConfigFilterRulesParam apiConfigFilterRulesIDTOToUpdateApiConfigFilterRulesParam(ApiConfigFilterRulesIDTO dto);

    @Named("apiConfigFilterRuleIDTOToUpdateApiConfigFilterRuleParam")
    UpdateApiConfigFilterRuleParam apiConfigFilterRuleIDTOToUpdateApiConfigFilterRuleParam(ApiConfigFilterRuleIDTO dto);

    @Named("apiConfigPluginConfigIDTOToUpdateApiConfigPluginConfigParam")
    UpdateApiConfigPluginConfigParam apiConfigPluginConfigIDTOToUpdateApiConfigPluginConfigParam(ApiConfigPluginConfigIDTO dto);

    @Named("apiConfigPluginChainItemIDTOToUpdateApiConfigPluginChainItemParam")
    UpdateApiConfigPluginChainItemParam apiConfigPluginChainItemIDTOToUpdateApiConfigPluginChainItemParam(ApiConfigPluginChainItemIDTO dto);

    @Named("taskReceiptConfigIDTOToUpdateTaskReceiptConfigParam")
    UpdateTaskReceiptConfigParam taskReceiptConfigIDTOToUpdateTaskReceiptConfigParam(TaskReceiptConfigIDTO dto);

    @Named("apiConfigExtraConfigIDTOToUpdateApiConfigExtraConfigParam")
    UpdateApiConfigExtraConfigParam apiConfigExtraConfigIDTOToUpdateApiConfigExtraConfigParam(ApiConfigExtraConfigIDTO dto);

    @Named("saveApiConfigRateLimitConfigParamToApiConfigRateLimitConfigIDTO")
    ApiConfigRateLimitConfigIDTO saveApiConfigRateLimitConfigParamToApiConfigRateLimitConfigIDTO(SaveApiConfigRateLimitConfigParam param);

    @Named("saveApiConfigRateLimitRuleParamToApiConfigRateLimitRuleIDTO")
    ApiConfigRateLimitRuleIDTO saveApiConfigRateLimitRuleParamToApiConfigRateLimitRuleIDTO(SaveApiConfigRateLimitRuleParam param);

    @Named("saveApiConfigFilterRulesParamToApiConfigFilterRulesIDTO")
    ApiConfigFilterRulesIDTO saveApiConfigFilterRulesParamToApiConfigFilterRulesIDTO(SaveApiConfigFilterRulesParam param);

    @Named("saveApiConfigFilterRuleParamToApiConfigFilterRuleIDTO")
    ApiConfigFilterRuleIDTO saveApiConfigFilterRuleParamToApiConfigFilterRuleIDTO(SaveApiConfigFilterRuleParam param);

    @Named("saveApiConfigPluginConfigParamToApiConfigPluginConfigIDTO")
    ApiConfigPluginConfigIDTO saveApiConfigPluginConfigParamToApiConfigPluginConfigIDTO(SaveApiConfigPluginConfigParam param);

    @Named("saveApiConfigPluginChainItemParamToApiConfigPluginChainItemIDTO")
    ApiConfigPluginChainItemIDTO saveApiConfigPluginChainItemParamToApiConfigPluginChainItemIDTO(SaveApiConfigPluginChainItemParam param);

    @Named("saveTaskReceiptConfigParamToTaskReceiptConfigIDTO")
    TaskReceiptConfigIDTO saveTaskReceiptConfigParamToTaskReceiptConfigIDTO(SaveTaskReceiptConfigParam param);

    @Named("saveApiConfigExtraConfigParamToApiConfigExtraConfigIDTO")
    ApiConfigExtraConfigIDTO saveApiConfigExtraConfigParamToApiConfigExtraConfigIDTO(SaveApiConfigExtraConfigParam param);

    @Named("updateApiConfigRateLimitConfigParamToApiConfigRateLimitConfigIDTO")
    ApiConfigRateLimitConfigIDTO updateApiConfigRateLimitConfigParamToApiConfigRateLimitConfigIDTO(UpdateApiConfigRateLimitConfigParam param);

    @Named("updateApiConfigRateLimitRuleParamToApiConfigRateLimitRuleIDTO")
    ApiConfigRateLimitRuleIDTO updateApiConfigRateLimitRuleParamToApiConfigRateLimitRuleIDTO(UpdateApiConfigRateLimitRuleParam param);

    @Named("updateApiConfigFilterRulesParamToApiConfigFilterRulesIDTO")
    ApiConfigFilterRulesIDTO updateApiConfigFilterRulesParamToApiConfigFilterRulesIDTO(UpdateApiConfigFilterRulesParam param);

    @Named("updateApiConfigFilterRuleParamToApiConfigFilterRuleIDTO")
    ApiConfigFilterRuleIDTO updateApiConfigFilterRuleParamToApiConfigFilterRuleIDTO(UpdateApiConfigFilterRuleParam param);

    @Named("updateApiConfigPluginConfigParamToApiConfigPluginConfigIDTO")
    ApiConfigPluginConfigIDTO updateApiConfigPluginConfigParamToApiConfigPluginConfigIDTO(UpdateApiConfigPluginConfigParam param);

    @Named("updateApiConfigPluginChainItemParamToApiConfigPluginChainItemIDTO")
    ApiConfigPluginChainItemIDTO updateApiConfigPluginChainItemParamToApiConfigPluginChainItemIDTO(UpdateApiConfigPluginChainItemParam param);

    @Named("updateTaskReceiptConfigParamToTaskReceiptConfigIDTO")
    TaskReceiptConfigIDTO updateTaskReceiptConfigParamToTaskReceiptConfigIDTO(UpdateTaskReceiptConfigParam param);

    @Named("updateApiConfigExtraConfigParamToApiConfigExtraConfigIDTO")
    ApiConfigExtraConfigIDTO updateApiConfigExtraConfigParamToApiConfigExtraConfigIDTO(UpdateApiConfigExtraConfigParam param);

}
