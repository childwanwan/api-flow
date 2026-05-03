package com.apigateway.domain.config.service;

import com.apigateway.common.exception.BusinessException;
import com.apigateway.common.exception.ErrorCode;
import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.domain.config.query.ApiConfigQuery;
import com.apigateway.domain.config.repository.ApiConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiConfigDomainService {

    private final ApiConfigRepository apiConfigRepository;

    public ApiConfigEntity createConfig(String groupNo, String apiCode, String apiName, String apiDescription,
                                         Long requestTimeoutMs, Integer autoRetryCount, Long retryIntervalMs,
                                         String rateLimitConfig, Integer maxQueueSize,
                                         String filterRules, String pluginConfig,
                                         String receiptConfig, String extraConfig,
                                         String operator) {
        ApiConfigQuery query = ApiConfigQuery.builder().apiCode(apiCode).build();
        ApiConfigEntity existing = apiConfigRepository.query(query);
        if (existing != null && !existing.getDeleted()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "API配置已存在");
        }
        ApiConfigEntity config = ApiConfigEntity.create(groupNo, apiCode, apiName, apiDescription,
                requestTimeoutMs, autoRetryCount, retryIntervalMs, rateLimitConfig, maxQueueSize,
                filterRules, pluginConfig, receiptConfig, extraConfig, operator);
        return apiConfigRepository.save(config);
    }

    public ApiConfigEntity updateConfig(String apiCode, String apiName, String apiDescription,
                                         Long requestTimeoutMs, Integer autoRetryCount, Long retryIntervalMs,
                                         String rateLimitConfig, Integer maxQueueSize,
                                         String filterRules, String pluginConfig,
                                         String receiptConfig, String extraConfig,
                                         String operator) {
        ApiConfigQuery query = ApiConfigQuery.builder().apiCode(apiCode).build();
        ApiConfigEntity config = apiConfigRepository.query(query);
        if (config == null || config.getDeleted()) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }
        config.update(apiName, apiDescription, requestTimeoutMs, autoRetryCount, retryIntervalMs,
                rateLimitConfig, maxQueueSize, filterRules, pluginConfig, receiptConfig, extraConfig, operator);
        return apiConfigRepository.update(config);
    }

    public ApiConfigEntity enableConfig(String apiCode, String operator) {
        ApiConfigQuery query = ApiConfigQuery.builder().apiCode(apiCode).build();
        ApiConfigEntity config = apiConfigRepository.query(query);
        if (config == null || config.getDeleted()) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }
        config.enable();
        config.setUpdateOperator(operator);
        return apiConfigRepository.update(config);
    }

    public ApiConfigEntity disableConfig(String apiCode, String operator) {
        ApiConfigQuery query = ApiConfigQuery.builder().apiCode(apiCode).build();
        ApiConfigEntity config = apiConfigRepository.query(query);
        if (config == null || config.getDeleted()) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }
        config.disable();
        config.setUpdateOperator(operator);
        return apiConfigRepository.update(config);
    }

    public ApiConfigEntity deleteConfig(String apiCode, String operator) {
        ApiConfigQuery query = ApiConfigQuery.builder().apiCode(apiCode).build();
        ApiConfigEntity config = apiConfigRepository.query(query);
        if (config == null || config.getDeleted()) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }
        config.setDeleted(true);
        config.setUpdateOperator(operator);
        config.setUpdateTimeMs(System.currentTimeMillis());
        return apiConfigRepository.update(config);
    }

    public ApiConfigEntity getConfig(String apiCode) {
        ApiConfigQuery query = ApiConfigQuery.builder().apiCode(apiCode).build();
        ApiConfigEntity config = apiConfigRepository.query(query);
        if (config == null || config.getDeleted()) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }
        return config;
    }

}
