package com.flow.domain.config.model;

import com.flow.api.repository.config.dto.ApiConfigExtraConfigIDTO;
import com.flow.api.repository.config.dto.ApiConfigFilterRulesIDTO;
import com.flow.api.repository.config.dto.ApiConfigPluginConfigIDTO;
import com.flow.api.repository.config.dto.ApiConfigRateLimitConfigIDTO;
import com.flow.api.repository.task.idto.TaskReceiptConfigIDTO;
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

    public boolean isEnabled() {
        return "ENABLED".equals(status);
    }

    public void enable() {
        this.status = "ENABLED";
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void disable() {
        this.status = "DISABLED";
        this.updateTimeMs = System.currentTimeMillis();
    }
}
