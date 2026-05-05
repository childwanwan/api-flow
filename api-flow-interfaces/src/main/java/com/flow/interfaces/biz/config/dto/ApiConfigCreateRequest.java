package com.flow.interfaces.biz.config.dto;

import com.flow.common.exception.BusinessException;
import com.flow.common.exception.ErrorCode;
import com.flow.interfaces.biz.config.dto.request.ApiConfigPluginConfigRequest;
import com.flow.interfaces.biz.config.dto.request.ApiConfigRateLimitConfigRequest;
import com.flow.interfaces.biz.config.dto.request.ApiConfigExtraConfigRequest;
import com.flow.interfaces.biz.config.dto.request.ApiConfigFilterRulesRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigCreateRequest {

    private String groupNo;
    private String apiCode;
    private String apiName;
    private String apiDescription;
    private Long requestTimeoutMs;
    private Integer autoRetryCount;
    private Long retryIntervalMs;
    private ApiConfigRateLimitConfigRequest rateLimitConfig;
    private Integer maxQueueSize;
    private ApiConfigFilterRulesRequest filterRules;
    private ApiConfigPluginConfigRequest pluginConfig;
    private ApiConfigExtraConfigRequest extraConfig;
    private String operator;

    public void validate() {
        validateSize(groupNo, 64, "groupNo");
        validateNotBlank(apiCode, "apiCode");
        validateSize(apiCode, 128, "apiCode");
        validatePattern(apiCode, "^[a-zA-Z0-9_-]+$", "apiCode");
        validateNotBlank(apiName, "apiName");
        validateSize(apiName, 256, "apiName");
        validateSize(apiDescription, 1024, "apiDescription");
        validateRange(requestTimeoutMs, 100L, 300000L, "requestTimeoutMs");
        validateNonNegative(autoRetryCount, "autoRetryCount");
        validateMax(autoRetryCount, 10, "autoRetryCount");
        validateRange(retryIntervalMs, 100L, 60000L, "retryIntervalMs");
        validateNonNegative(maxQueueSize, "maxQueueSize");
        validateMax(maxQueueSize, 100000, "maxQueueSize");
        validateSize(operator, 64, "operator");

        if (rateLimitConfig != null) {
            rateLimitConfig.validate();
        }
        if (filterRules != null) {
            filterRules.validate();
        }
        if (pluginConfig != null) {
            pluginConfig.validate();
        }
        if (extraConfig != null) {
            extraConfig.validate();
        }
    }

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " cannot be blank");
        }
    }

    private void validateSize(String value, int max, String fieldName) {
        if (value != null && value.length() > max) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " length must be less than or equal to " + max);
        }
    }

    private void validatePattern(String value, String regex, String fieldName) {
        if (value != null && !value.matches(regex)) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " can only contain letters, numbers, underscore and hyphen");
        }
    }

    private void validateRange(Long value, Long min, Long max, String fieldName) {
        if (value != null && (value < min || value > max)) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " must be between " + min + " and " + max);
        }
    }

    private void validateRange(Integer value, Integer min, Integer max, String fieldName) {
        if (value != null && (value < min || value > max)) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " must be between " + min + " and " + max);
        }
    }

    private void validateNonNegative(Integer value, String fieldName) {
        if (value != null && value < 0) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " must be non-negative");
        }
    }

    private void validateMax(Integer value, int max, String fieldName) {
        if (value != null && value > max) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " must be at most " + max);
        }
    }
}