package com.apiflow.domain.config.model;

import com.apiflow.common.enums.EnableStatus;
import com.apiflow.domain.task.model.ReceiptConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigDO {

    private Long id;
    private String groupNo;
    private String apiCode;
    private String apiName;
    private String apiDescription;
    private String status;
    private Long requestTimeoutMs;
    private Integer autoRetryCount;
    private Long retryIntervalMs;
    private RateLimitConfig rateLimitConfig;
    private Integer maxQueueSize;
    private FilterRules filterRules;
    private PluginConfig pluginConfig;
    private ReceiptConfig receiptConfig;
    private ExtraConfig extraConfig;
    private Long createTimeMs;
    private Long updateTimeMs;
    private String createOperator;
    private String updateOperator;
    private Boolean deleted;
    private Integer version;

    public boolean isEnabled() {
        return EnableStatus.ENABLED.getValue().equals(status);
    }

    public void enable() {
        this.status = EnableStatus.ENABLED.getValue();
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void disable() {
        this.status = EnableStatus.DISABLED.getValue();
        this.updateTimeMs = System.currentTimeMillis();
    }
}
