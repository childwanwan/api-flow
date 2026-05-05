package com.apiflow.interfaces.biz.config.request;

import com.apiflow.interfaces.util.ValidationHelper;
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
        ValidationHelper.validateSize(groupNo, 64, "groupNo");
        ValidationHelper.validateNotBlank(apiCode, "apiCode");
        ValidationHelper.validateSize(apiCode, 128, "apiCode");
        ValidationHelper.validatePattern(apiCode, "^[a-zA-Z0-9_-]+$", "apiCode");
        ValidationHelper.validateNotBlank(apiName, "apiName");
        ValidationHelper.validateSize(apiName, 256, "apiName");
        ValidationHelper.validateSize(apiDescription, 1024, "apiDescription");
        ValidationHelper.validateRange(requestTimeoutMs, 100L, 300000L, "requestTimeoutMs");
        ValidationHelper.validateNonNegative(autoRetryCount, "autoRetryCount");
        ValidationHelper.validateMax(autoRetryCount, 10, "autoRetryCount");
        ValidationHelper.validateRange(retryIntervalMs, 100L, 60000L, "retryIntervalMs");
        ValidationHelper.validateNonNegative(maxQueueSize, "maxQueueSize");
        ValidationHelper.validateMax(maxQueueSize, 100000, "maxQueueSize");
        ValidationHelper.validateSize(operator, 64, "operator");

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
}
