package com.apiflow.application.config;

import com.apiflow.application.config.command.ApiConfigCreateCommand;
import com.apiflow.application.config.command.ApiConfigDeleteCommand;
import com.apiflow.application.config.command.ApiConfigDisableCommand;
import com.apiflow.application.config.command.ApiConfigEnableCommand;
import com.apiflow.application.config.command.ApiConfigUpdateCommand;
import com.apiflow.application.config.dto.*;
import com.apiflow.application.configlog.ConfigChangeLogService;
import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.api.repository.config.idto.ApiConfigIDTO;
import com.apiflow.api.repository.config.param.SelectApiConfigParam;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.domain.config.converter.ApiConfigConverter;
import com.apiflow.domain.config.model.ApiConfigDO;
import com.apiflow.domain.config.service.ApiConfigDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiConfigApplicationService {

    private final ApiConfigDomainService apiConfigDomainService;
    private final ApiConfigRepository apiConfigRepository;
    private final ConfigChangeLogService configChangeLogService;
    private static final ApiConfigConverter API_CONFIG_CONVERTER = ApiConfigConverter.INSTANCE;

    public ApiConfigDTO createConfig(ApiConfigCreateCommand command) {
        ApiConfigDO config = apiConfigDomainService.createConfig(
                command.getGroupNo(),
                command.getApiCode(),
                command.getApiName(),
                command.getApiDescription(),
                command.getRequestTimeoutMs(),
                command.getAutoRetryCount(),
                command.getRetryIntervalMs(),
                command.getMaxQueueSize(),
                command.getOperator()
        );
        saveChangeLog(command.getApiCode(), "CREATE", null, config, command.getOperator());
        return toDTO(config);
    }

    public ApiConfigDTO updateConfig(ApiConfigUpdateCommand command) {
        ApiConfigDO beforeConfig = apiConfigDomainService.getConfig(command.getApiCode());
        ApiConfigDO config = apiConfigDomainService.updateConfig(
                command.getApiCode(),
                command.getApiName(),
                command.getApiDescription(),
                command.getRequestTimeoutMs(),
                command.getAutoRetryCount(),
                command.getRetryIntervalMs(),
                command.getMaxQueueSize(),
                command.getOperator()
        );
        saveChangeLog(command.getApiCode(), "UPDATE", beforeConfig, config, command.getOperator());
        return toDTO(config);
    }

    public ApiConfigDTO getConfig(String apiCode) {
        ApiConfigDO config = apiConfigDomainService.getConfig(apiCode);
        return toDTO(config);
    }

    public List<ApiConfigDTO> listConfigs(String apiCode, String apiName, String status, Integer limit) {
        SelectApiConfigParam.SelectApiConfigParamBuilder builder = SelectApiConfigParam.builder();
        if (apiCode != null && !apiCode.isEmpty()) {
            builder.apiCode(FieldCondition.of(apiCode));
        }
        if (apiName != null && !apiName.isEmpty()) {
            builder.apiName(FieldCondition.<String>builder().like(apiName).build());
        }
        if (status != null && !status.isEmpty()) {
            builder.status(FieldCondition.of(status));
        }
        List<ApiConfigIDTO> configs = apiConfigRepository.selectList(builder.build());
        return configs.stream().map(idto -> toDTO(API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(idto))).toList();
    }

    public ApiConfigDTO enableConfig(ApiConfigEnableCommand command) {
        ApiConfigDO beforeConfig = apiConfigDomainService.getConfig(command.getApiCode());
        ApiConfigDO config = apiConfigDomainService.enableConfig(command.getApiCode(), command.getOperator());
        saveChangeLog(command.getApiCode(), "ENABLE", beforeConfig, config, command.getOperator());
        return toDTO(config);
    }

    public ApiConfigDTO disableConfig(ApiConfigDisableCommand command) {
        ApiConfigDO beforeConfig = apiConfigDomainService.getConfig(command.getApiCode());
        ApiConfigDO config = apiConfigDomainService.disableConfig(command.getApiCode(), command.getOperator());
        saveChangeLog(command.getApiCode(), "DISABLE", beforeConfig, config, command.getOperator());
        return toDTO(config);
    }

    public ApiConfigDTO deleteConfig(ApiConfigDeleteCommand command) {
        ApiConfigDO beforeConfig = apiConfigDomainService.getConfig(command.getApiCode());
        ApiConfigDO config = apiConfigDomainService.deleteConfig(command.getApiCode(), command.getOperator());
        saveChangeLog(command.getApiCode(), "DELETE", beforeConfig, config, command.getOperator());
        return toDTO(config);
    }

    private void saveChangeLog(String apiCode, String changeType, ApiConfigDO before, ApiConfigDO after, String operator) {
        try {
            String beforeJson = before != null ? JsonUtil.toJson(before) : null;
            String afterJson = after != null ? JsonUtil.toJson(after) : null;
            configChangeLogService.saveLog(apiCode, changeType, beforeJson, afterJson, operator);
        } catch (Exception e) {
            log.error("Failed to save config change log: apiCode={}, changeType={}", apiCode, changeType, e);
        }
    }

    private ApiConfigDTO toDTO(ApiConfigDO config) {
        return ApiConfigDTO.builder()
                .id(config.getId())
                .groupNo(config.getGroupNo())
                .apiCode(config.getApiCode())
                .apiName(config.getApiName())
                .apiDescription(config.getApiDescription())
                .status(config.getStatus())
                .requestTimeoutMs(config.getRequestTimeoutMs())
                .autoRetryCount(config.getAutoRetryCount())
                .retryIntervalMs(config.getRetryIntervalMs())
                .rateLimitConfig(toRateLimitConfigDTO(config.getRateLimitConfig()))
                .maxQueueSize(config.getMaxQueueSize())
                .filterRules(toFilterRulesDTO(config.getFilterRules()))
                .pluginConfig(toPluginConfigDTO(config.getPluginConfig()))
                .receiptConfig(null)
                .extraConfig(toExtraConfigDTO(config.getExtraConfig()))
                .createTimeMs(config.getCreateTimeMs())
                .updateTimeMs(config.getUpdateTimeMs())
                .createOperator(config.getCreateOperator())
                .updateOperator(config.getUpdateOperator())
                .build();
    }

    private RateLimitConfigDTO toRateLimitConfigDTO(com.apiflow.domain.config.model.RateLimitConfig config) {
        if (config == null) return null;
        return RateLimitConfigDTO.builder()
                .enabled(config.getEnabled())
                .rules(config.getRules() != null
                        ? config.getRules().stream().map(this::toRateLimitRuleDTO).toList()
                        : null)
                .build();
    }

    private RateLimitRuleDTO toRateLimitRuleDTO(com.apiflow.domain.config.model.RateLimitRule rule) {
        return RateLimitRuleDTO.builder()
                .name(rule.getName())
                .type(rule.getType())
                .dimension(rule.getDimension())
                .keyTemplate(rule.getKeyTemplate())
                .limit(rule.getLimit())
                .windowSeconds(rule.getWindowSeconds())
                .build();
    }

    private FilterRulesDTO toFilterRulesDTO(com.apiflow.domain.config.model.FilterRules rules) {
        if (rules == null) return null;
        return FilterRulesDTO.builder()
                .rules(rules.getRules() != null
                        ? rules.getRules().stream().map(this::toFilterRuleDTO).toList()
                        : null)
                .build();
    }

    private FilterRuleDTO toFilterRuleDTO(com.apiflow.domain.config.model.FilterRule rule) {
        return FilterRuleDTO.builder()
                .field(rule.getField())
                .operator(rule.getOperator())
                .value(rule.getValue())
                .message(rule.getMessage())
                .build();
    }

    private PluginConfigDTO toPluginConfigDTO(com.apiflow.domain.config.model.PluginConfig config) {
        if (config == null) return null;
        return PluginConfigDTO.builder()
                .enabled(config.getEnabled())
                .pluginChain(config.getPluginChain() != null
                        ? config.getPluginChain().stream().map(this::toPluginChainItemDTO).toList()
                        : null)
                .build();
    }

    private PluginChainItemDTO toPluginChainItemDTO(com.apiflow.domain.config.model.PluginChainItem item) {
        return PluginChainItemDTO.builder()
                .pluginCode(item.getPluginCode())
                .order(item.getOrder())
                .enabled(item.getEnabled())
                .config(item.getConfig())
                .build();
    }

    private ExtraConfigDTO toExtraConfigDTO(com.apiflow.domain.config.model.ExtraConfig config) {
        if (config == null) return null;
        return ExtraConfigDTO.builder()
                .region(config.getRegion())
                .sellerId(config.getSellerId())
                .awsAccessKey(config.getAwsAccessKey())
                .environment(config.getEnvironment())
                .build();
    }

}
