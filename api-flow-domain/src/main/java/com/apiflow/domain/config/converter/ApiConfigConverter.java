package com.apiflow.domain.config.converter;

import com.apiflow.api.repository.config.idto.*;
import com.apiflow.api.repository.task.idto.TaskHttpReceiptIDTO;
import com.apiflow.api.repository.task.idto.TaskMqReceiptIDTO;
import com.apiflow.api.repository.task.idto.TaskReceiptConfigIDTO;
import com.apiflow.api.repository.task.idto.TaskReceiptRetryPolicyIDTO;
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

    @Named("apiConfigIDTOToApiConfig")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "rateLimitConfigIDTOToDomain")
    @Mapping(target = "filterRules", qualifiedByName = "filterRulesIDTOToDomain")
    @Mapping(target = "pluginConfig", qualifiedByName = "pluginConfigIDTOToDomain")
    @Mapping(target = "receiptConfig", qualifiedByName = "receiptConfigIDTOToDomain")
    @Mapping(target = "extraConfig", qualifiedByName = "extraConfigIDTOToDomain")
    ApiConfig apiConfigIDTOToApiConfig(ApiConfigIDTO dto);

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
