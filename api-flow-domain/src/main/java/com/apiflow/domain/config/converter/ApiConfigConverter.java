package com.apiflow.domain.config.converter;

import com.apiflow.api.repository.config.idto.*;
import com.apiflow.api.repository.config.param.*;
import com.apiflow.api.repository.task.idto.TaskHttpReceiptIDTO;
import com.apiflow.api.repository.task.idto.TaskMqReceiptIDTO;
import com.apiflow.api.repository.task.idto.TaskReceiptConfigIDTO;
import com.apiflow.api.repository.task.idto.TaskReceiptRetryPolicyIDTO;
import com.apiflow.api.repository.task.param.SaveTaskReceiptConfigParam;
import com.apiflow.api.repository.task.param.UpdateTaskReceiptConfigParam;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.domain.config.model.*;
import com.apiflow.domain.task.model.HttpReceipt;
import com.apiflow.domain.task.model.MqReceipt;
import com.apiflow.domain.task.model.ReceiptConfig;
import com.apiflow.domain.task.model.ReceiptRetryPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(implementationName = "DomainApiConfigConverter")
public interface ApiConfigConverter {

    ApiConfigConverter INSTANCE = Mappers.getMapper(ApiConfigConverter.class);

    @Named("apiConfigIDTOToApiConfigDO")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "rateLimitConfigIDTOToDomain")
    @Mapping(target = "filterRules", qualifiedByName = "filterRulesIDTOToDomain")
    @Mapping(target = "pluginConfig", qualifiedByName = "pluginConfigIDTOToDomain")
    @Mapping(target = "receiptConfig", qualifiedByName = "receiptConfigIDTOToDomain")
    @Mapping(target = "extraConfig", qualifiedByName = "extraConfigIDTOToDomain")
    ApiConfigDO apiConfigIDTOToApiConfigDO(ApiConfigIDTO dto);

    @Named("apiConfigDOToSaveApiConfigParam")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "rateLimitConfigToSaveParam")
    @Mapping(target = "filterRules", qualifiedByName = "filterRulesToSaveParam")
    @Mapping(target = "pluginConfig", qualifiedByName = "pluginConfigToSaveParam")
    @Mapping(target = "receiptConfig", qualifiedByName = "receiptConfigToSaveParam")
    @Mapping(target = "extraConfig", qualifiedByName = "extraConfigToSaveParam")
    SaveApiConfigParam apiConfigDOToSaveApiConfigParam(ApiConfigDO configDO);

    @Named("apiConfigDOToUpdateApiConfigParam")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "rateLimitConfigToUpdateParam")
    @Mapping(target = "filterRules", qualifiedByName = "filterRulesToUpdateParam")
    @Mapping(target = "pluginConfig", qualifiedByName = "pluginConfigToUpdateParam")
    @Mapping(target = "receiptConfig", qualifiedByName = "receiptConfigToUpdateParam")
    @Mapping(target = "extraConfig", qualifiedByName = "extraConfigToUpdateParam")
    UpdateApiConfigParam apiConfigDOToUpdateApiConfigParam(ApiConfigDO configDO);

    @Named("rateLimitConfigIDTOToDomain")
    RateLimitConfig rateLimitConfigIDTOToDomain(ApiConfigRateLimitConfigIDTO dto);

    @Named("rateLimitRuleIDTOToDomain")
    RateLimitRule rateLimitRuleIDTOToDomain(ApiConfigRateLimitRuleIDTO dto);

    @Named("filterRulesIDTOToDomain")
    FilterRules filterRulesIDTOToDomain(ApiConfigFilterRulesIDTO dto);

    @Named("filterRuleIDTOToDomain")
    FilterRule filterRuleIDTOToDomain(ApiConfigFilterRuleIDTO dto);

    @Named("pluginConfigIDTOToDomain")
    PluginConfig pluginConfigIDTOToDomain(ApiConfigPluginConfigIDTO dto);

    @Named("pluginChainItemIDTOToDomain")
    PluginChainItem pluginChainItemIDTOToDomain(ApiConfigPluginChainItemIDTO dto);

    @Named("extraConfigIDTOToDomain")
    ExtraConfig extraConfigIDTOToDomain(ApiConfigExtraConfigIDTO dto);

    @Named("receiptConfigIDTOToDomain")
    ReceiptConfig receiptConfigIDTOToDomain(TaskReceiptConfigIDTO dto);

    @Named("httpReceiptIDTOToDomain")
    HttpReceipt httpReceiptIDTOToDomain(TaskHttpReceiptIDTO dto);

    @Named("mqReceiptIDTOToDomain")
    MqReceipt mqReceiptIDTOToDomain(TaskMqReceiptIDTO dto);

    @Named("retryPolicyIDTOToDomain")
    ReceiptRetryPolicy retryPolicyIDTOToDomain(TaskReceiptRetryPolicyIDTO dto);

    @Named("rateLimitConfigToSaveParam")
    SaveApiConfigRateLimitConfigParam rateLimitConfigToSaveParam(RateLimitConfig config);

    @Named("rateLimitRuleToSaveParam")
    SaveApiConfigRateLimitRuleParam rateLimitRuleToSaveParam(RateLimitRule rule);

    @Named("filterRulesToSaveParam")
    SaveApiConfigFilterRulesParam filterRulesToSaveParam(FilterRules rules);

    @Named("filterRuleToSaveParam")
    SaveApiConfigFilterRuleParam filterRuleToSaveParam(FilterRule rule);

    @Named("pluginConfigToSaveParam")
    SaveApiConfigPluginConfigParam pluginConfigToSaveParam(PluginConfig config);

    @Named("pluginChainItemToSaveParam")
    SaveApiConfigPluginChainItemParam pluginChainItemToSaveParam(PluginChainItem item);

    @Named("extraConfigToSaveParam")
    SaveApiConfigExtraConfigParam extraConfigToSaveParam(ExtraConfig config);

    @Named("receiptConfigToSaveParam")
    SaveTaskReceiptConfigParam receiptConfigToSaveParam(ReceiptConfig config);

    @Named("rateLimitConfigToUpdateParam")
    UpdateApiConfigRateLimitConfigParam rateLimitConfigToUpdateParam(RateLimitConfig config);

    @Named("rateLimitRuleToUpdateParam")
    UpdateApiConfigRateLimitRuleParam rateLimitRuleToUpdateParam(RateLimitRule rule);

    @Named("filterRulesToUpdateParam")
    UpdateApiConfigFilterRulesParam filterRulesToUpdateParam(FilterRules rules);

    @Named("filterRuleToUpdateParam")
    UpdateApiConfigFilterRuleParam filterRuleToUpdateParam(FilterRule rule);

    @Named("pluginConfigToUpdateParam")
    UpdateApiConfigPluginConfigParam pluginConfigToUpdateParam(PluginConfig config);

    @Named("pluginChainItemToUpdateParam")
    UpdateApiConfigPluginChainItemParam pluginChainItemToUpdateParam(PluginChainItem item);

    @Named("extraConfigToUpdateParam")
    UpdateApiConfigExtraConfigParam extraConfigToUpdateParam(ExtraConfig config);

    @Named("receiptConfigToUpdateParam")
    UpdateTaskReceiptConfigParam receiptConfigToUpdateParam(ReceiptConfig config);

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
