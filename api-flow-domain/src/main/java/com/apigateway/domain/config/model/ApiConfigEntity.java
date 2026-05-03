package com.apigateway.domain.config.model;

import com.apigateway.common.constant.SystemConstant;
import com.apigateway.domain.config.enums.ApiConfigStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigEntity {

    private Long id;
    private String groupNo;
    private String apiCode;
    private String apiName;
    private String apiDescription;
    private ApiConfigStatus status;
    private Long requestTimeoutMs;
    private Integer autoRetryCount;
    private Long retryIntervalMs;
    private String rateLimitConfig;
    private Integer maxQueueSize;
    private String filterRules;
    private String pluginConfig;
    private String receiptConfig;
    private String extraConfig;
    private Long createTimeMs;
    private Long updateTimeMs;
    private String createOperator;
    private String updateOperator;
    private Boolean deleted;
    private Integer version;

    public void enable() {
        this.status = ApiConfigStatus.ENABLED;
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void disable() {
        this.status = ApiConfigStatus.DISABLED;
        this.updateTimeMs = System.currentTimeMillis();
    }

    public boolean isEnabled() {
        return this.status != null && this.status.isEnabled();
    }

    public static ApiConfigEntity create(String groupNo, String apiCode, String apiName, String apiDescription,
                                        Long requestTimeoutMs, Integer autoRetryCount, Long retryIntervalMs,
                                        String rateLimitConfig, Integer maxQueueSize,
                                        String filterRules, String pluginConfig,
                                        String receiptConfig, String extraConfig,
                                        String operator) {
        long now = System.currentTimeMillis();
        return ApiConfigEntity.builder()
                .groupNo(groupNo)
                .apiCode(apiCode)
                .apiName(apiName)
                .apiDescription(apiDescription)
                .status(ApiConfigStatus.ENABLED)
                .requestTimeoutMs(requestTimeoutMs == null ? SystemConstant.DEFAULT_REQUEST_TIMEOUT_MS : requestTimeoutMs)
                .autoRetryCount(autoRetryCount == null ? SystemConstant.DEFAULT_AUTO_RETRY_COUNT : autoRetryCount)
                .retryIntervalMs(retryIntervalMs == null ? SystemConstant.DEFAULT_RETRY_INTERVAL_MS : retryIntervalMs)
                .rateLimitConfig(rateLimitConfig)
                .maxQueueSize(maxQueueSize == null ? 100000 : maxQueueSize)
                .filterRules(filterRules)
                .pluginConfig(pluginConfig)
                .receiptConfig(receiptConfig)
                .extraConfig(extraConfig)
                .createTimeMs(now)
                .updateTimeMs(now)
                .createOperator(operator)
                .updateOperator(operator)
                .deleted(false)
                .version(0)
                .build();
    }

    public void update(String apiName, String apiDescription, Long requestTimeoutMs, Integer autoRetryCount, Long retryIntervalMs,
                       String rateLimitConfig, Integer maxQueueSize, String filterRules, String pluginConfig,
                       String receiptConfig, String extraConfig, String operator) {
        if (apiName != null) {
            this.apiName = apiName;
        }
        if (apiDescription != null) {
            this.apiDescription = apiDescription;
        }
        if (requestTimeoutMs != null) {
            this.requestTimeoutMs = requestTimeoutMs;
        }
        if (autoRetryCount != null) {
            this.autoRetryCount = autoRetryCount;
        }
        if (retryIntervalMs != null) {
            this.retryIntervalMs = retryIntervalMs;
        }
        if (rateLimitConfig != null) {
            this.rateLimitConfig = rateLimitConfig;
        }
        if (maxQueueSize != null) {
            this.maxQueueSize = maxQueueSize;
        }
        if (filterRules != null) {
            this.filterRules = filterRules;
        }
        if (pluginConfig != null) {
            this.pluginConfig = pluginConfig;
        }
        if (receiptConfig != null) {
            this.receiptConfig = receiptConfig;
        }
        if (extraConfig != null) {
            this.extraConfig = extraConfig;
        }
        this.updateOperator = operator;
        this.updateTimeMs = System.currentTimeMillis();
    }

}
