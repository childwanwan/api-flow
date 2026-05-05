package com.apiflow.application.config.dto;

import com.apiflow.application.task.dto.ReceiptConfigDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigDTO {

    private Long id;
    private String groupNo;
    private String apiCode;
    private String apiName;
    private String apiDescription;
    private String status;
    private Long requestTimeoutMs;
    private Integer autoRetryCount;
    private Long retryIntervalMs;
    private RateLimitConfigDTO rateLimitConfig;
    private Integer maxQueueSize;
    private FilterRulesDTO filterRules;
    private PluginConfigDTO pluginConfig;
    private ReceiptConfigDTO receiptConfig;
    private ExtraConfigDTO extraConfig;
    private Long createTimeMs;
    private Long updateTimeMs;
    private String createOperator;
    private String updateOperator;
}
