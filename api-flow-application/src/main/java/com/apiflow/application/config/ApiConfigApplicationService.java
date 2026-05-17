package com.apiflow.application.config;

import com.apiflow.application.LockHelper;
import com.apiflow.application.config.command.ApiConfigCreateCommand;
import com.apiflow.application.config.command.ApiConfigDeleteCommand;
import com.apiflow.application.config.command.ApiConfigDisableCommand;
import com.apiflow.application.config.command.ApiConfigEnableCommand;
import com.apiflow.application.config.command.ApiConfigUpdateCommand;
import com.apiflow.application.config.dto.*;
import com.apiflow.application.configlog.ConfigChangeLogService;
import com.apiflow.application.task.dto.HttpReceiptDTO;
import com.apiflow.application.task.dto.MqReceiptDTO;
import com.apiflow.application.task.dto.ReceiptConfigDTO;
import com.apiflow.application.task.dto.ReceiptRetryPolicyDTO;
import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.api.repository.config.idto.ApiConfigIDTO;
import com.apiflow.api.repository.config.param.SelectApiConfigParam;
import com.apiflow.common.constant.LockPrefix;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.domain.config.command.CreateApiConfigCommand;
import com.apiflow.domain.config.command.DeleteApiConfigCommand;
import com.apiflow.domain.config.command.UpdateApiConfigCommand;
import com.apiflow.domain.config.converter.ApiConfigConverter;
import com.apiflow.domain.config.model.ApiConfig;
import com.apiflow.domain.config.model.ExtraConfig;
import com.apiflow.domain.config.service.ApiConfigDomainService;
import com.apiflow.domain.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiConfigApplicationService {

    private static final String LOCK_PREFIX = LockPrefix.API_CONFIG;

    private final ApiConfigDomainService apiConfigDomainService;
    private final ApiConfigRepository apiConfigRepository;
    private final ConfigChangeLogService configChangeLogService;
    private final DomainEventPublisher domainEventPublisher;
    private final LockHelper lockHelper;
    private static final ApiConfigConverter API_CONFIG_CONVERTER = ApiConfigConverter.INSTANCE;

    public ApiConfigDTO createConfig(ApiConfigCreateCommand command) {
        return lockHelper.execute(LOCK_PREFIX, command.getApiCode(), () -> {
            ExtraConfig extraConfig = parseExtraConfig(command.getExtraConfig());
            com.apiflow.domain.task.model.ReceiptConfig receiptConfig = parseReceiptConfig(command.getReceiptConfig());
            com.apiflow.domain.config.model.RateLimitConfig rateLimitConfig = parseRateLimitConfig(command.getRateLimitConfig());
            com.apiflow.domain.config.model.FilterRules filterRules = parseFilterRules(command.getFilterRules());
            com.apiflow.domain.config.model.PluginConfig pluginConfig = parsePluginConfig(command.getPluginConfig());

            CreateApiConfigCommand domainCommand = CreateApiConfigCommand.builder()
                    .groupNo(command.getGroupNo())
                    .apiCode(command.getApiCode())
                    .apiName(command.getApiName())
                    .apiDescription(command.getApiDescription())
                    .status(command.getStatus())
                    .requestTimeoutMs(command.getRequestTimeoutMs())
                    .autoRetryCount(command.getAutoRetryCount())
                    .retryIntervalMs(command.getRetryIntervalMs())
                    .maxQueueSize(command.getMaxQueueSize())
                    .extraConfig(extraConfig)
                    .rateLimitConfig(rateLimitConfig)
                    .filterRules(filterRules)
                    .pluginConfig(pluginConfig)
                    .receiptConfig(receiptConfig)
                    .operator(command.getOperator())
                    .build();

            ApiConfig config = apiConfigDomainService.create(domainCommand);
            apiConfigRepository.save(API_CONFIG_CONVERTER.apiConfigToSaveApiConfigParam(config));
            domainEventPublisher.publishAll(config);
            return toDTO(config);
        });
    }

    public ApiConfigDTO updateConfig(ApiConfigUpdateCommand command) {
        return lockHelper.execute(LOCK_PREFIX, command.getApiCode(), () -> {
            ApiConfig beforeConfig = apiConfigDomainService.getConfig(command.getApiCode());
            ExtraConfig extraConfig = parseExtraConfig(command.getExtraConfig());
            com.apiflow.domain.task.model.ReceiptConfig receiptConfig = parseReceiptConfig(command.getReceiptConfig());
            com.apiflow.domain.config.model.RateLimitConfig rateLimitConfig = parseRateLimitConfig(command.getRateLimitConfig());
            com.apiflow.domain.config.model.FilterRules filterRules = parseFilterRules(command.getFilterRules());
            com.apiflow.domain.config.model.PluginConfig pluginConfig = parsePluginConfig(command.getPluginConfig());

            UpdateApiConfigCommand domainCommand = UpdateApiConfigCommand.builder()
                    .apiCode(command.getApiCode())
                    .groupNo(command.getGroupNo())
                    .apiName(command.getApiName())
                    .apiDescription(command.getApiDescription())
                    .status(command.getStatus())
                    .requestTimeoutMs(command.getRequestTimeoutMs())
                    .autoRetryCount(command.getAutoRetryCount())
                    .retryIntervalMs(command.getRetryIntervalMs())
                    .maxQueueSize(command.getMaxQueueSize())
                    .extraConfig(extraConfig)
                    .rateLimitConfig(rateLimitConfig)
                    .filterRules(filterRules)
                    .pluginConfig(pluginConfig)
                    .receiptConfig(receiptConfig)
                    .operator(command.getOperator())
                    .build();

            ApiConfig config = apiConfigDomainService.update(beforeConfig, domainCommand);
            apiConfigRepository.update(API_CONFIG_CONVERTER.apiConfigToUpdateApiConfigParam(config));
            domainEventPublisher.publishAll(config);
            return toDTO(config);
        });
    }

    public ApiConfigDTO getConfig(String apiCode) {
        ApiConfig config = apiConfigDomainService.getConfig(apiCode);
        return toDTO(config);
    }

    public List<ApiConfigDTO> listConfigs(String groupNo, String apiCode, String apiName, String status, Integer limit) {
        SelectApiConfigParam.SelectApiConfigParamBuilder builder = SelectApiConfigParam.builder();
        if (groupNo != null && !groupNo.isEmpty()) {
            builder.groupNo(FieldCondition.of(groupNo));
        }
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
        return configs.stream().map(idto -> toDTO(API_CONFIG_CONVERTER.apiConfigIDTOToApiConfig(idto))).toList();
    }

    public ApiConfigDTO enableConfig(ApiConfigEnableCommand command) {
        return lockHelper.execute(LOCK_PREFIX, command.getApiCode(), () -> {
            ApiConfig config = apiConfigDomainService.getConfig(command.getApiCode());
            config = apiConfigDomainService.enable(config, command.getOperator());
            apiConfigRepository.update(API_CONFIG_CONVERTER.apiConfigToUpdateApiConfigParam(config));
            domainEventPublisher.publishAll(config);
            return toDTO(config);
        });
    }

    public ApiConfigDTO disableConfig(ApiConfigDisableCommand command) {
        return lockHelper.execute(LOCK_PREFIX, command.getApiCode(), () -> {
            ApiConfig config = apiConfigDomainService.getConfig(command.getApiCode());
            config = apiConfigDomainService.disable(config, command.getOperator());
            apiConfigRepository.update(API_CONFIG_CONVERTER.apiConfigToUpdateApiConfigParam(config));
            domainEventPublisher.publishAll(config);
            return toDTO(config);
        });
    }

    public void deleteConfig(ApiConfigDeleteCommand command) {
        lockHelper.executeVoid(LOCK_PREFIX, command.getApiCode(), () -> {
            ApiConfig config = apiConfigDomainService.getConfig(command.getApiCode());
            DeleteApiConfigCommand domainCommand = DeleteApiConfigCommand.builder()
                    .apiCode(command.getApiCode())
                    .operator(command.getOperator())
                    .build();
            config = apiConfigDomainService.delete(config, domainCommand);
            apiConfigRepository.update(API_CONFIG_CONVERTER.apiConfigToUpdateApiConfigParam(config));
            domainEventPublisher.publishAll(config);
        });
    }

    private ApiConfigDTO toDTO(ApiConfig config) {
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
                .receiptConfig(toReceiptConfigDTO(config.getReceiptConfig()))
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
                .envConfig(config.getEnvConfig())
                .targetUrl(config.getTargetUrl())
                .targetMethod(config.getTargetMethod())
                .targetHeaders(config.getTargetHeaders())
                .targetBodyTemplate(config.getTargetBodyTemplate())
                .targetTimeoutMs(config.getTargetTimeoutMs())
                .build();
    }

    private ReceiptConfigDTO toReceiptConfigDTO(com.apiflow.domain.task.model.ReceiptConfig config) {
        if (config == null) return null;
        return ReceiptConfigDTO.builder()
                .receiptTypes(config.getReceiptTypes())
                .http(config.getHttp() != null ? toHttpReceiptDTO(config.getHttp()) : null)
                .mq(config.getMq() != null ? toMqReceiptDTO(config.getMq()) : null)
                .build();
    }

    private HttpReceiptDTO toHttpReceiptDTO(com.apiflow.domain.task.model.HttpReceipt http) {
        return HttpReceiptDTO.builder()
                .url(http.getUrl())
                .method(http.getMethod())
                .headers(http.getHeaders())
                .bodyTemplate(http.getBodyTemplate())
                .retryPolicy(http.getRetryPolicy() != null ? toReceiptRetryPolicyDTO(http.getRetryPolicy()) : null)
                .timeoutMs(http.getTimeoutMs())
                .build();
    }

    private MqReceiptDTO toMqReceiptDTO(com.apiflow.domain.task.model.MqReceipt mq) {
        return MqReceiptDTO.builder()
                .topic(mq.getTopic())
                .keyTemplate(mq.getKeyTemplate())
                .headers(mq.getHeaders())
                .bodyTemplate(mq.getBodyTemplate())
                .build();
    }

    private ReceiptRetryPolicyDTO toReceiptRetryPolicyDTO(com.apiflow.domain.task.model.ReceiptRetryPolicy policy) {
        return ReceiptRetryPolicyDTO.builder()
                .maxRetries(policy.getMaxRetries())
                .retryIntervals(policy.getRetryIntervals())
                .build();
    }

    private ExtraConfig parseExtraConfig(String extraConfigJson) {
        if (extraConfigJson == null || extraConfigJson.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(extraConfigJson, ExtraConfig.class);
        } catch (Exception e) {
            log.warn("Failed to parse extraConfig JSON: {}", extraConfigJson, e);
            return null;
        }
    }

    private com.apiflow.domain.task.model.ReceiptConfig parseReceiptConfig(String receiptConfigJson) {
        if (receiptConfigJson == null || receiptConfigJson.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(receiptConfigJson, com.apiflow.domain.task.model.ReceiptConfig.class);
        } catch (Exception e) {
            log.warn("Failed to parse receiptConfig JSON: {}", receiptConfigJson, e);
            return null;
        }
    }

    private com.apiflow.domain.config.model.RateLimitConfig parseRateLimitConfig(String rateLimitConfigJson) {
        if (rateLimitConfigJson == null || rateLimitConfigJson.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(rateLimitConfigJson, com.apiflow.domain.config.model.RateLimitConfig.class);
        } catch (Exception e) {
            log.warn("Failed to parse rateLimitConfig JSON: {}", rateLimitConfigJson, e);
            return null;
        }
    }

    private com.apiflow.domain.config.model.FilterRules parseFilterRules(String filterRulesJson) {
        if (filterRulesJson == null || filterRulesJson.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(filterRulesJson, com.apiflow.domain.config.model.FilterRules.class);
        } catch (Exception e) {
            log.warn("Failed to parse filterRules JSON: {}", filterRulesJson, e);
            return null;
        }
    }

    private com.apiflow.domain.config.model.PluginConfig parsePluginConfig(String pluginConfigJson) {
        if (pluginConfigJson == null || pluginConfigJson.isEmpty()) {
            return null;
        }
        try {
            return JsonUtil.toObject(pluginConfigJson, com.apiflow.domain.config.model.PluginConfig.class);
        } catch (Exception e) {
            log.warn("Failed to parse pluginConfig JSON: {}", pluginConfigJson, e);
            return null;
        }
    }

}
