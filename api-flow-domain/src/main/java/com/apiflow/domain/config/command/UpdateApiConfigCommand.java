package com.apiflow.domain.config.command;

import com.apiflow.domain.config.model.ExtraConfig;
import com.apiflow.domain.config.model.FilterRules;
import com.apiflow.domain.config.model.PluginConfig;
import com.apiflow.domain.config.model.RateLimitConfig;
import com.apiflow.domain.task.model.ReceiptConfig;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateApiConfigCommand {

    private final String apiCode;
    private final String groupNo;
    private final String apiName;
    private final String apiDescription;
    private final String status;
    private final Long requestTimeoutMs;
    private final Integer autoRetryCount;
    private final Long retryIntervalMs;
    private final Integer maxQueueSize;
    private final RateLimitConfig rateLimitConfig;
    private final FilterRules filterRules;
    private final PluginConfig pluginConfig;
    private final ReceiptConfig receiptConfig;
    private final ExtraConfig extraConfig;
    private final String operator;
}
