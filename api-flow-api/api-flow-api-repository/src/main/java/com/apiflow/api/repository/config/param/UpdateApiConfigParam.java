package com.apiflow.api.repository.config.param;

import com.apiflow.api.repository.task.param.UpdateTaskReceiptConfigParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApiConfigParam {
    private Long id;
    private String groupNo;
    private String apiCode;
    private String apiName;
    private String apiDescription;
    private String status;
    private Long requestTimeoutMs;
    private Integer autoRetryCount;
    private Long retryIntervalMs;
    private UpdateApiConfigRateLimitConfigParam rateLimitConfig;
    private Integer maxQueueSize;
    private UpdateApiConfigFilterRulesParam filterRules;
    private UpdateApiConfigPluginConfigParam pluginConfig;
    private UpdateTaskReceiptConfigParam receiptConfig;
    private UpdateApiConfigExtraConfigParam extraConfig;
}
