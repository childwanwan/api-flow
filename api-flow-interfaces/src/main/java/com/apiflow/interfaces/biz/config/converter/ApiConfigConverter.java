package com.apiflow.interfaces.biz.config.converter;

import com.apiflow.application.config.param.ApiConfigPageParam;
import com.apiflow.application.config.param.CreateApiConfigParam;
import com.apiflow.application.config.param.UpdateApiConfigParam;
import com.apiflow.application.config.dto.*;
import com.apiflow.application.task.dto.HttpReceiptDTO;
import com.apiflow.application.task.dto.MqReceiptDTO;
import com.apiflow.application.task.dto.ReceiptConfigDTO;
import com.apiflow.application.task.dto.ReceiptRetryPolicyDTO;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.interfaces.biz.config.request.ApiConfigCreateRequest;
import com.apiflow.interfaces.biz.config.request.ApiConfigPageRequest;
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
    @Mapping(target = "receiptConfig", qualifiedByName = "requestToJson")
    CreateApiConfigParam toCreateCommand(ApiConfigCreateRequest request);

    @Named("toUpdateCommand")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "requestToJson")
    @Mapping(target = "filterRules", qualifiedByName = "requestToJson")
    @Mapping(target = "pluginConfig", qualifiedByName = "requestToJson")
    @Mapping(target = "extraConfig", qualifiedByName = "requestToJson")
    @Mapping(target = "receiptConfig", qualifiedByName = "requestToJson")
    UpdateApiConfigParam toUpdateCommand(ApiConfigUpdateRequest request);

    default ApiConfigPageParam toPageParam(ApiConfigPageRequest request) {
        if (request == null) {
            return null;
        }
        ApiConfigPageParam param = new ApiConfigPageParam();
        param.setCurrent(request.getEffectiveCurrent());
        param.setSize(request.getEffectiveSize());
        param.setSortOrderList(request.getSortOrderList());
        param.setGroupNo(request.getGroupNo());
        param.setGroupNoLike(request.getGroupNoLike());
        param.setApiCodeLike(request.getApiCodeLike());
        param.setApiNameLike(request.getApiNameLike());
        param.setStatus(request.getStatus());
        return param;
    }

    @Named("toVO")
    @Mapping(target = "rateLimitConfig", source = "rateLimitConfig")
    @Mapping(target = "filterRules", source = "filterRules")
    @Mapping(target = "pluginConfig", source = "pluginConfig")
    @Mapping(target = "extraConfig", source = "extraConfig")
    @Mapping(target = "receiptConfig", source = "receiptConfig")
    ApiConfigVO toVO(ApiConfigDTO dto);

    ApiConfigRateLimitConfigVO toRateLimitConfigVO(RateLimitConfigDTO dto);

    ApiConfigRateLimitRuleVO toRateLimitRuleVO(RateLimitRuleDTO dto);

    ApiConfigFilterRulesVO toFilterRulesVO(FilterRulesDTO dto);

    ApiConfigFilterRuleVO toFilterRuleVO(FilterRuleDTO dto);

    ApiConfigPluginConfigVO toPluginConfigVO(PluginConfigDTO dto);

    @Mapping(target = "config", qualifiedByName = "objectToString")
    ApiConfigPluginChainItemVO toPluginChainItemVO(PluginChainItemDTO dto);

    ApiConfigExtraConfigVO toExtraConfigVO(ExtraConfigDTO dto);

    ApiConfigReceiptConfigVO toReceiptConfigVO(ReceiptConfigDTO dto);

    ApiConfigReceiptConfigVO.HttpReceiptVO toHttpReceiptVO(HttpReceiptDTO dto);

    ApiConfigReceiptConfigVO.MqReceiptVO toMqReceiptVO(MqReceiptDTO dto);

    ApiConfigReceiptConfigVO.RetryPolicyVO toRetryPolicyVO(ReceiptRetryPolicyDTO dto);

    @Named("requestToJson")
    default String requestToJson(Object obj) {
        return obj == null ? null : JsonUtil.toJson(obj);
    }

    @Named("objectToString")
    default String objectToString(Object obj) {
        return obj != null ? obj.toString() : null;
    }
}
