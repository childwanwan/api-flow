package com.apiflow.api.repository.config.param;

import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiConfigField implements FieldMetadata {
    ID("id", "id", null),
    GROUP_NO("groupNo", "group_no", null),
    API_CODE("apiCode", "api_code", null),
    API_NAME("apiName", "api_name", null),
    API_DESCRIPTION("apiDescription", "api_description", null),
    STATUS("status", "status", null),
    REQUEST_TIMEOUT_MS("requestTimeoutMs", "request_timeout_ms", null),
    AUTO_RETRY_COUNT("autoRetryCount", "auto_retry_count", null),
    RETRY_INTERVAL_MS("retryIntervalMs", "retry_interval_ms", null),
    MAX_QUEUE_SIZE("maxQueueSize", "max_queue_size", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null),
    UPDATE_TIME_MS("updateTimeMs", "update_time_ms", null),
    CREATE_OPERATOR("createOperator", "create_operator", null),
    UPDATE_OPERATOR("updateOperator", "update_operator", null),
    DELETED("deleted", "deleted", null),
    VERSION("version", "version", null),
    RATE_LIMIT_CONFIG("rateLimitConfig", "rate_limit_config", null),
    RATE_LIMIT_CONFIG_MAX_REQUESTS("rateLimitConfig.maxRequests", "rate_limit_config", "maxRequests"),
    RATE_LIMIT_CONFIG_TIME_WINDOW_MS("rateLimitConfig.timeWindowMs", "rate_limit_config", "timeWindowMs"),
    FILTER_RULES("filterRules", "filter_rules", null),
    PLUGIN_CONFIG("pluginConfig", "plugin_config", null),
    RECEIPT_CONFIG("receiptConfig", "receipt_config", null),
    EXTRA_CONFIG("extraConfig", "extra_config", null);

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
