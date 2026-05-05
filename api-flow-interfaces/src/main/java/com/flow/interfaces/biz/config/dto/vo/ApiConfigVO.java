package com.flow.interfaces.biz.config.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigVO {
    private Long id;
    private String groupNo;
    private String apiCode;
    private String apiName;
    private String apiDescription;
    private String status;
    private Long requestTimeoutMs;
    private Integer autoRetryCount;
    private Long retryIntervalMs;
    private ApiConfigRateLimitConfigVO rateLimitConfig;
    private Integer maxQueueSize;
    private ApiConfigFilterRulesVO filterRules;
    private ApiConfigPluginConfigVO pluginConfig;
    private ApiConfigExtraConfigVO extraConfig;
    private Long createTimeMs;
    private Long updateTimeMs;
    private String createOperator;
    private String updateOperator;
}
