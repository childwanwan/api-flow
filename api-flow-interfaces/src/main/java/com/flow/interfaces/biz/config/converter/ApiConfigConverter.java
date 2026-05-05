package com.flow.interfaces.biz.config.converter;

import com.flow.application.config.command.ApiConfigCreateCommand;
import com.flow.application.config.command.ApiConfigUpdateCommand;
import com.flow.api.repository.config.dto.ApiConfigExtraConfigIDTO;
import com.flow.api.repository.config.dto.ApiConfigFilterRulesIDTO;
import com.flow.api.repository.config.dto.ApiConfigPluginConfigIDTO;
import com.flow.api.repository.config.dto.ApiConfigRateLimitConfigIDTO;
import com.flow.common.util.JsonUtil;
import com.flow.domain.config.model.ApiConfigDO;
import com.flow.interfaces.biz.config.dto.ApiConfigCreateRequest;
import com.flow.interfaces.biz.config.dto.ApiConfigUpdateRequest;
import com.flow.interfaces.biz.config.dto.vo.*;
import com.flow.interfaces.biz.config.dto.request.*;
import com.flow.interfaces.biz.config.dto.vo.*;
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
    @Mapping(target = "rateLimitConfig", qualifiedByName = "rateLimitConfigIDTOToVO")
    @Mapping(target = "filterRules", qualifiedByName = "filterRulesIDTOToVO")
    @Mapping(target = "pluginConfig", qualifiedByName = "pluginConfigIDTOToVO")
    @Mapping(target = "extraConfig", qualifiedByName = "extraConfigIDTOToVO")
    ApiConfigVO toVO(ApiConfigDO configDO);

    @Named("requestToJson")
    default String requestToJson(Object obj) {
        return JsonUtil.toJson(obj);
    }

    @Named("rateLimitConfigIDTOToVO")
    ApiConfigRateLimitConfigVO rateLimitConfigIDTOToVO(ApiConfigRateLimitConfigIDTO dto);

    ApiConfigRateLimitRuleVO rateLimitRuleIDTOToVO(com.flow.api.repository.config.dto.ApiConfigRateLimitRuleIDTO dto);

    @Named("filterRulesIDTOToVO")
    ApiConfigFilterRulesVO filterRulesIDTOToVO(ApiConfigFilterRulesIDTO dto);

    ApiConfigFilterRuleVO filterRuleIDTOToVO(com.flow.api.repository.config.dto.ApiConfigFilterRuleIDTO dto);

    @Named("pluginConfigIDTOToVO")
    ApiConfigPluginConfigVO pluginConfigIDTOToVO(ApiConfigPluginConfigIDTO dto);

    @Mapping(target = "config", qualifiedByName = "objectToString")
    ApiConfigPluginChainItemVO pluginChainItemIDTOToVO(com.flow.api.repository.config.dto.ApiConfigPluginChainItemIDTO dto);

    @Named("extraConfigIDTOToVO")
    ApiConfigExtraConfigVO extraConfigIDTOToVO(ApiConfigExtraConfigIDTO dto);

    @Named("objectToString")
    default String objectToString(Object obj) {
        return obj != null ? obj.toString() : null;
    }
}
