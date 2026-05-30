package com.apiflow.application.config.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApiConfigParam {
    private String groupNo;
    private String apiCode;
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
