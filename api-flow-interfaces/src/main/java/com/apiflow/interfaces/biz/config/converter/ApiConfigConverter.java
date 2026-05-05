package com.apiflow.interfaces.biz.config.converter;

import com.apiflow.application.config.command.ApiConfigCreateCommand;
import com.apiflow.application.config.command.ApiConfigUpdateCommand;
import com.apiflow.application.config.dto.*;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.interfaces.biz.config.request.ApiConfigCreateRequest;
import com.apiflow.interfaces.biz.config.request.ApiConfigUpdateRequest;
import com.apiflow.interfaces.biz.config.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(implementationName = "InterfacesApiConfigConverter")
public interface ApiConfigConverter {

    ApiConfigConverter INSTANCE = Mappers.getMapper(ApiConfigConverter.class);

    @Named("toCreateCommand")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "requestToJson")
    @Mapping(target = "filterRules", qualifiedByName = "requestToJson")
    @Mapping(target = "pluginConfig", qualifiedByName = "requestToJson")
    @Mapping(target = "extraConfig", qualifiedByName = "requestToJson")
    @Mapping(target = "receiptConfig", ignore = true)
    ApiConfigCreateCommand toCreateCommand(ApiConfigCreateRequest request);

    @Named("toUpdateCommand")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "requestToJson")
    @Mapping(target = "filterRules", qualifiedByName = "requestToJson")
    @Mapping(target = "pluginConfig", qualifiedByName = "requestToJson")
    @Mapping(target = "extraConfig", qualifiedByName = "requestToJson")
    @Mapping(target = "receiptConfig", ignore = true)
    ApiConfigUpdateCommand toUpdateCommand(ApiConfigUpdateRequest request);

    @Named("toVO")
    @Mapping(target = "rateLimitConfig", source = "rateLimitConfig")
    @Mapping(target = "filterRules", source = "filterRules")
    @Mapping(target = "pluginConfig", source = "pluginConfig")
    @Mapping(target = "extraConfig", source = "extraConfig")
    ApiConfigVO toVO(ApiConfigDTO dto);

    ApiConfigRateLimitConfigVO toRateLimitConfigVO(RateLimitConfigDTO dto);

    ApiConfigRateLimitRuleVO toRateLimitRuleVO(RateLimitRuleDTO dto);

    ApiConfigFilterRulesVO toFilterRulesVO(FilterRulesDTO dto);

    ApiConfigFilterRuleVO toFilterRuleVO(FilterRuleDTO dto);

    ApiConfigPluginConfigVO toPluginConfigVO(PluginConfigDTO dto);

    @Mapping(target = "config", qualifiedByName = "objectToString")
    ApiConfigPluginChainItemVO toPluginChainItemVO(PluginChainItemDTO dto);

    ApiConfigExtraConfigVO toExtraConfigVO(ExtraConfigDTO dto);

    @Named("requestToJson")
    default String requestToJson(Object obj) {
        return obj == null ? null : JsonUtil.toJson(obj);
    }

    @Named("objectToString")
    default String objectToString(Object obj) {
        return obj != null ? obj.toString() : null;
    }
}
