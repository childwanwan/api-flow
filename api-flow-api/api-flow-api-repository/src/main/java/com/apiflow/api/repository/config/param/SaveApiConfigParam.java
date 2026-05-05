package com.apiflow.api.repository.config.param;

import com.apiflow.api.repository.task.param.SaveTaskReceiptConfigParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveApiConfigParam {
    private String groupNo;
    private String apiCode;
    private String apiName;
    private String apiDescription;
    private String status;
    private Long requestTimeoutMs;
    private Integer autoRetryCount;
    private Long retryIntervalMs;
    private SaveApiConfigRateLimitConfigParam rateLimitConfig;
    private Integer maxQueueSize;
    private SaveApiConfigFilterRulesParam filterRules;
    private SaveApiConfigPluginConfigParam pluginConfig;
    private SaveTaskReceiptConfigParam receiptConfig;
    private SaveApiConfigExtraConfigParam extraConfig;
}
