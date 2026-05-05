package com.apiflow.api.repository.config.idto;

import com.apiflow.api.repository.task.idto.TaskReceiptConfigIDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigIDTO {
    private Long id;
    private String groupNo;
    private String apiCode;
    private String apiName;
    private String apiDescription;
    private String status;
    private Long requestTimeoutMs;
    private Integer autoRetryCount;
    private Long retryIntervalMs;
    private ApiConfigRateLimitConfigIDTO rateLimitConfig;
    private Integer maxQueueSize;
    private ApiConfigFilterRulesIDTO filterRules;
    private ApiConfigPluginConfigIDTO pluginConfig;
    private TaskReceiptConfigIDTO receiptConfig;
    private ApiConfigExtraConfigIDTO extraConfig;
    private Long createTimeMs;
    private Long updateTimeMs;
    private String createOperator;
    private String updateOperator;
    private Boolean deleted;
    private Integer version;
}
