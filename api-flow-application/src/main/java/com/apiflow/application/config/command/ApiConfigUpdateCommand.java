package com.apiflow.application.config.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiConfigUpdateCommand {
    private String apiCode;
    private String groupNo;
    private String apiName;
    private String apiDescription;
    private String status;
    private Long requestTimeoutMs;
    private Integer autoRetryCount;
    private Long retryIntervalMs;
    private String rateLimitConfig;
    private Integer maxQueueSize;
    private String filterRules;
    private String pluginConfig;
    private String receiptConfig;
    private String extraConfig;
    private String operator;
}
