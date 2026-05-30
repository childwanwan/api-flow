package com.apiflow.application.config.converter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.apiflow.api.repository.config.idto.*;
import com.apiflow.api.repository.config.param.*;
import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.task.idto.TaskHttpReceiptIDTO;
import com.apiflow.api.repository.task.idto.TaskMqReceiptIDTO;
import com.apiflow.api.repository.task.idto.TaskReceiptConfigIDTO;
import com.apiflow.api.repository.task.idto.TaskReceiptRetryPolicyIDTO;
import com.apiflow.api.repository.task.param.SaveTaskReceiptConfigParam;
import com.apiflow.api.repository.task.param.UpdateTaskReceiptConfigParam;
import com.apiflow.application.config.param.CreateApiConfigParam;
import com.apiflow.application.config.param.DeleteApiConfigParam;
import com.apiflow.application.config.param.UpdateApiConfigParam;
import com.apiflow.application.config.dto.*;
import com.apiflow.application.config.param.ApiConfigPageParam;
import com.apiflow.application.config.param.ListApiConfigParam;
import com.apiflow.application.task.dto.HttpReceiptDTO;
import com.apiflow.application.task.dto.MqReceiptDTO;
import com.apiflow.application.task.dto.ReceiptConfigDTO;
import com.apiflow.application.task.dto.ReceiptRetryPolicyDTO;
import com.apiflow.common.dto.SortOrder;
import com.apiflow.common.enums.EnableStatus;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.domain.config.command.CreateApiConfigCommand;
import com.apiflow.domain.config.command.DeleteApiConfigCommand;
import com.apiflow.domain.config.command.UpdateApiConfigCommand;
import com.apiflow.domain.config.model.*;
import com.apiflow.domain.task.model.HttpReceipt;
import com.apiflow.domain.task.model.MqReceipt;
import com.apiflow.domain.task.model.ReceiptConfig;
import com.apiflow.domain.task.model.ReceiptRetryPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(implementationName = "ApplicationApiConfigConverter")
public interface ApiConfigConverter {

    Logger log = LoggerFactory.getLogger(ApiConfigConverter.class);

    ApiConfigConverter INSTANCE = Mappers.getMapper(ApiConfigConverter.class);

    default SelectPageApiConfigParam apiConfigPageParam2SelectPageApiConfigParam(ApiConfigPageParam param) {
        if (ObjectUtil.isEmpty(param)) {
            return null;
        }
        SelectPageApiConfigParam.SelectPageApiConfigParamBuilder paramBuilder = SelectPageApiConfigParam.builder();
        paramBuilder.selectFields(List.of(ApiConfigField.API_CODE,
                ApiConfigField.API_NAME,
                ApiConfigField.GROUP_NO,
                ApiConfigField.STATUS,
                ApiConfigField.EXTRA_CONFIG,
                ApiConfigField.CREATE_TIME_MS,
                ApiConfigField.UPDATE_TIME_MS));

        ConditionNode condition = buildConditionNode(param);
        if (condition != null) {
            paramBuilder.condition(condition);
        }

        paramBuilder.current(param.getEffectiveCurrent()).size(param.getEffectiveSize());

        List<OrderBy> orders = convertSortOrderList(param.getSortOrderList());
        if (!CollectionUtils.isEmpty(orders)) {
            paramBuilder.orders(orders);
        }
        return paramBuilder.build();
    }

    private ConditionNode buildConditionNode(ApiConfigPageParam param) {
        List<ConditionNode> nodes = new ArrayList<>();
        if (StrUtil.isNotEmpty(param.getGroupNo())) {
            nodes.add(ConditionNode.eq(ApiConfigField.GROUP_NO.getFieldName(), param.getGroupNo()));
        }
        if (StrUtil.isNotEmpty(param.getGroupNoLike())) {
            nodes.add(ConditionNode.like(ApiConfigField.GROUP_NO.getFieldName(), param.getGroupNoLike()));
        }
        if (StrUtil.isNotEmpty(param.getApiCodeLike())) {
            nodes.add(ConditionNode.like(ApiConfigField.API_CODE.getFieldName(), param.getApiCodeLike()));
        }
        if (StrUtil.isNotEmpty(param.getApiNameLike())) {
            nodes.add(ConditionNode.like(ApiConfigField.API_NAME.getFieldName(), param.getApiNameLike()));
        }
        if (StrUtil.isNotEmpty(param.getStatus())) {
            nodes.add(ConditionNode.eq(ApiConfigField.STATUS.getFieldName(), param.getStatus()));
        }
        if (nodes.isEmpty()) {
            return null;
        }
        return ConditionNode.and(nodes.toArray(new ConditionNode[0]));
    }

    private List<OrderBy> convertSortOrderList(List<SortOrder> sortOrderList) {
        if (CollectionUtils.isEmpty(sortOrderList)) {
            return List.of(OrderBy.desc(ApiConfigField.CREATE_TIME_MS), OrderBy.desc(ApiConfigField.ID));
        }

        return sortOrderList.stream()
                .filter(order -> StrUtil.isNotEmpty(order.getField()))
                .map(order -> {
                    try {
                        ApiConfigField field = ApiConfigField.valueOf(order.getField().toUpperCase());
                        return OrderBy.builder()
                                .field(field)
                                .ascending(ObjectUtil.isNotEmpty(order.getAscending()) ? order.getAscending() : true)
                                .order(order.getOrder())
                                .build();
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(ObjectUtil::isNotEmpty)
                .sorted(Comparator.comparing(order -> ObjectUtil.isNotEmpty(order.getOrder()) ? order.getOrder() : Integer.MAX_VALUE))
                .collect(Collectors.toList());
    }

    default SelectApiConfigParam buildSelectApiConfigParam(ListApiConfigParam param) {
        List<ConditionNode> nodes = new ArrayList<>();
        if (StrUtil.isNotEmpty(param.getGroupNo())) {
            nodes.add(ConditionNode.eq(ApiConfigField.GROUP_NO.getFieldName(), param.getGroupNo()));
        }
        if (StrUtil.isNotEmpty(param.getApiCode())) {
            nodes.add(ConditionNode.eq(ApiConfigField.API_CODE.getFieldName(), param.getApiCode()));
        }
        if (StrUtil.isNotEmpty(param.getApiName())) {
            nodes.add(ConditionNode.like(ApiConfigField.API_NAME.getFieldName(), param.getApiName()));
        }
        if (StrUtil.isNotEmpty(param.getStatus())) {
            nodes.add(ConditionNode.eq(ApiConfigField.STATUS.getFieldName(), param.getStatus()));
        }
        ConditionNode condition = nodes.isEmpty() ? null : ConditionNode.and(nodes.toArray(new ConditionNode[0]));
        return SelectApiConfigParam.builder()
                .selectFields(List.of(ApiConfigField.API_CODE, ApiConfigField.API_NAME, ApiConfigField.GROUP_NO, ApiConfigField.STATUS, ApiConfigField.CREATE_TIME_MS, ApiConfigField.UPDATE_TIME_MS))
                .condition(condition).build();
    }

    @Mapping(target = "rateLimitConfig", qualifiedByName = "rateLimitConfigIDTOToDTO")
    @Mapping(target = "filterRules", qualifiedByName = "filterRulesIDTOToDTO")
    @Mapping(target = "pluginConfig", qualifiedByName = "pluginConfigIDTOToDTO")
    @Mapping(target = "receiptConfig", qualifiedByName = "receiptConfigIDTOToDTO")
    @Mapping(target = "extraConfig", qualifiedByName = "extraConfigIDTOToDTO")
    @Mapping(target = "groupCode", ignore = true)
    @Mapping(target = "groupName", ignore = true)
    ApiConfigDTO idtoToDTO(ApiConfigIDTO idto);

    default List<ApiConfigDTO> idtosToDTOs(List<ApiConfigIDTO> idtos, Map<String, ApiGroupIDTO> groupMap) {
        if (CollectionUtils.isEmpty(idtos)) {
            return List.of();
        }
        return idtos.stream().map(idto -> {
            ApiConfigDTO dto = idtoToDTO(idto);
            if (dto != null && StrUtil.isNotBlank(dto.getGroupNo())) {
                ApiGroupIDTO group = groupMap.get(dto.getGroupNo());
                if (group != null) {
                    dto.setGroupCode(group.getGroupCode());
                    dto.setGroupName(group.getGroupName());
                }
            }
            return dto;
        }).toList();
    }

    @Named("rateLimitConfigIDTOToDTO")
    RateLimitConfigDTO rateLimitConfigIDTOToDTO(ApiConfigRateLimitConfigIDTO dto);

    RateLimitRuleDTO rateLimitRuleIDTOToDTO(ApiConfigRateLimitRuleIDTO dto);

    @Named("filterRulesIDTOToDTO")
    FilterRulesDTO filterRulesIDTOToDTO(ApiConfigFilterRulesIDTO dto);

    FilterRuleDTO filterRuleIDTOToDTO(ApiConfigFilterRuleIDTO dto);

    @Named("pluginConfigIDTOToDTO")
    PluginConfigDTO pluginConfigIDTOToDTO(ApiConfigPluginConfigIDTO dto);

    PluginChainItemDTO pluginChainItemIDTOToDTO(ApiConfigPluginChainItemIDTO dto);

    @Named("extraConfigIDTOToDTO")
    ExtraConfigDTO extraConfigIDTOToDTO(ApiConfigExtraConfigIDTO dto);

    @Named("receiptConfigIDTOToDTO")
    ReceiptConfigDTO receiptConfigIDTOToDTO(TaskReceiptConfigIDTO dto);

    HttpReceiptDTO httpReceiptIDTOToDTO(TaskHttpReceiptIDTO dto);

    MqReceiptDTO mqReceiptIDTOToDTO(TaskMqReceiptIDTO dto);

    ReceiptRetryPolicyDTO retryPolicyIDTOToDTO(TaskReceiptRetryPolicyIDTO dto);

    @Named("apiConfigToSaveApiConfigParam")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "rateLimitConfigToSaveParam")
    @Mapping(target = "filterRules", qualifiedByName = "filterRulesToSaveParam")
    @Mapping(target = "pluginConfig", qualifiedByName = "pluginConfigToSaveParam")
    @Mapping(target = "receiptConfig", qualifiedByName = "receiptConfigToSaveParam")
    @Mapping(target = "extraConfig", qualifiedByName = "extraConfigToSaveParam")
    SaveApiConfigParam apiConfigToSaveApiConfigParam(ApiConfig config);

    @Named("apiConfigToUpdateApiConfigParam")
    @Mapping(target = "rateLimitConfig", qualifiedByName = "rateLimitConfigToUpdateParam")
    @Mapping(target = "filterRules", qualifiedByName = "filterRulesToUpdateParam")
    @Mapping(target = "pluginConfig", qualifiedByName = "pluginConfigToUpdateParam")
    @Mapping(target = "receiptConfig", qualifiedByName = "receiptConfigToUpdateParam")
    @Mapping(target = "extraConfig", qualifiedByName = "extraConfigToUpdateParam")
    com.apiflow.api.repository.config.param.UpdateApiConfigParam apiConfigToUpdateApiConfigParam(ApiConfig config);

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

    ApiConfigDTO toDTO(ApiConfig config);

    RateLimitConfigDTO toRateLimitConfigDTO(RateLimitConfig config);

    RateLimitRuleDTO toRateLimitRuleDTO(RateLimitRule rule);

    FilterRulesDTO toFilterRulesDTO(FilterRules rules);

    FilterRuleDTO toFilterRuleDTO(FilterRule rule);

    PluginConfigDTO toPluginConfigDTO(PluginConfig config);

    PluginChainItemDTO toPluginChainItemDTO(PluginChainItem item);

    ExtraConfigDTO toExtraConfigDTO(ExtraConfig config);

    ReceiptConfigDTO toReceiptConfigDTO(ReceiptConfig config);

    HttpReceiptDTO toHttpReceiptDTO(HttpReceipt http);

    MqReceiptDTO toMqReceiptDTO(MqReceipt mq);

    ReceiptRetryPolicyDTO toReceiptRetryPolicyDTO(ReceiptRetryPolicy policy);

    default CreateApiConfigCommand toCreateDomainCommand(CreateApiConfigParam param) {
        return CreateApiConfigCommand.builder()
                .groupNo(param.getGroupNo())
                .apiCode(param.getApiCode())
                .apiName(param.getApiName())
                .apiDescription(param.getApiDescription())
                .status(StrUtil.isNotBlank(param.getStatus()) ? param.getStatus() : EnableStatus.ENABLED.getValue())
                .requestTimeoutMs(param.getRequestTimeoutMs() != null ? param.getRequestTimeoutMs() : 30000L)
                .autoRetryCount(param.getAutoRetryCount() != null ? param.getAutoRetryCount() : 64)
                .retryIntervalMs(param.getRetryIntervalMs() != null ? param.getRetryIntervalMs() : 5000L)
                .maxQueueSize(param.getMaxQueueSize() != null ? param.getMaxQueueSize() : 100000)
                .extraConfig(parseExtraConfig(param.getExtraConfig()))
                .rateLimitConfig(parseRateLimitConfig(param.getRateLimitConfig()))
                .filterRules(parseFilterRules(param.getFilterRules()))
                .pluginConfig(parsePluginConfig(param.getPluginConfig()))
                .receiptConfig(parseReceiptConfig(param.getReceiptConfig()))
                .operator(param.getOperator())
                .build();
    }

    default UpdateApiConfigCommand toUpdateDomainCommand(UpdateApiConfigParam param) {
        return UpdateApiConfigCommand.builder()
                .apiCode(param.getApiCode())
                .groupNo(param.getGroupNo())
                .apiName(param.getApiName())
                .apiDescription(param.getApiDescription())
                .status(param.getStatus())
                .requestTimeoutMs(param.getRequestTimeoutMs())
                .autoRetryCount(param.getAutoRetryCount())
                .retryIntervalMs(param.getRetryIntervalMs())
                .maxQueueSize(param.getMaxQueueSize())
                .extraConfig(parseExtraConfig(param.getExtraConfig()))
                .rateLimitConfig(parseRateLimitConfig(param.getRateLimitConfig()))
                .filterRules(parseFilterRules(param.getFilterRules()))
                .pluginConfig(parsePluginConfig(param.getPluginConfig()))
                .receiptConfig(parseReceiptConfig(param.getReceiptConfig()))
                .operator(param.getOperator())
                .build();
    }

    default DeleteApiConfigCommand toDeleteDomainCommand(DeleteApiConfigParam param) {
        return DeleteApiConfigCommand.builder()
                .apiCode(param.getApiCode())
                .operator(param.getOperator())
                .build();
    }

    @Named("parseExtraConfig")
    default ExtraConfig parseExtraConfig(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(json, ExtraConfig.class);
        } catch (Exception e) {
            log.warn("Failed to parse extraConfig JSON: {}", json, e);
            return null;
        }
    }

    @Named("parseReceiptConfig")
    default ReceiptConfig parseReceiptConfig(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(json, ReceiptConfig.class);
        } catch (Exception e) {
            log.warn("Failed to parse receiptConfig JSON: {}", json, e);
            return null;
        }
    }

    @Named("parseRateLimitConfig")
    default RateLimitConfig parseRateLimitConfig(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(json, RateLimitConfig.class);
        } catch (Exception e) {
            log.warn("Failed to parse rateLimitConfig JSON: {}", json, e);
            return null;
        }
    }

    @Named("parseFilterRules")
    default FilterRules parseFilterRules(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(json, FilterRules.class);
        } catch (Exception e) {
            log.warn("Failed to parse filterRules JSON: {}", json, e);
            return null;
        }
    }

    @Named("parsePluginConfig")
    default PluginConfig parsePluginConfig(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(json, PluginConfig.class);
        } catch (Exception e) {
            log.warn("Failed to parse pluginConfig JSON: {}", json, e);
            return null;
        }
    }
}
