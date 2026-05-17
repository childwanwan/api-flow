package com.apiflow.domain.config.model;

import cn.hutool.core.util.StrUtil;
import com.apiflow.common.enums.EnableStatus;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.domain.config.command.CreateApiConfigCommand;
import com.apiflow.domain.config.command.DeleteApiConfigCommand;
import com.apiflow.domain.config.command.UpdateApiConfigCommand;
import com.apiflow.domain.config.event.ApiConfigDomainEvent;
import com.apiflow.domain.shared.model.AggregateRoot;
import com.apiflow.domain.task.model.ReceiptConfig;
import lombok.Getter;

@Getter
public class ApiConfig extends AggregateRoot {

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

    private ApiConfig() {
    }

    public static ApiConfig create(CreateApiConfigCommand command) {
        validateApiCode(command.getApiCode());
        validateApiName(command.getApiName());

        ApiConfig config = new ApiConfig();
        config.groupNo = command.getGroupNo();
        config.apiCode = command.getApiCode();
        config.apiName = command.getApiName();
        config.apiDescription = command.getApiDescription();
        config.status = StrUtil.isNotBlank(command.getStatus())
                ? command.getStatus()
                : EnableStatus.ENABLED.getValue();
        config.requestTimeoutMs = command.getRequestTimeoutMs() != null ? command.getRequestTimeoutMs() : 30000L;
        config.autoRetryCount = command.getAutoRetryCount() != null ? command.getAutoRetryCount() : 64;
        config.retryIntervalMs = command.getRetryIntervalMs() != null ? command.getRetryIntervalMs() : 5000L;
        config.maxQueueSize = command.getMaxQueueSize() != null ? command.getMaxQueueSize() : 100000;
        config.rateLimitConfig = command.getRateLimitConfig();
        config.filterRules = command.getFilterRules();
        config.pluginConfig = command.getPluginConfig();
        config.receiptConfig = command.getReceiptConfig();
        config.extraConfig = command.getExtraConfig();
        long now = System.currentTimeMillis();
        config.createTimeMs = now;
        config.updateTimeMs = now;
        config.createOperator = command.getOperator();
        config.updateOperator = command.getOperator();
        config.deleted = false;
        config.version = 0;

        config.registerEvent(new ApiConfigDomainEvent.Created(
                config.apiCode, config.apiName, command.getOperator()
        ));

        return config;
    }

    public static ReconstituteBuilder reconstitute() {
        return new ReconstituteBuilder();
    }

    public static class ReconstituteBuilder {
        private final ApiConfig config = new ApiConfig();

        public ReconstituteBuilder id(Long id) { config.id = id; return this; }
        public ReconstituteBuilder groupNo(String groupNo) { config.groupNo = groupNo; return this; }
        public ReconstituteBuilder apiCode(String apiCode) { config.apiCode = apiCode; return this; }
        public ReconstituteBuilder apiName(String apiName) { config.apiName = apiName; return this; }
        public ReconstituteBuilder apiDescription(String apiDescription) { config.apiDescription = apiDescription; return this; }
        public ReconstituteBuilder status(String status) { config.status = status; return this; }
        public ReconstituteBuilder requestTimeoutMs(Long requestTimeoutMs) { config.requestTimeoutMs = requestTimeoutMs; return this; }
        public ReconstituteBuilder autoRetryCount(Integer autoRetryCount) { config.autoRetryCount = autoRetryCount; return this; }
        public ReconstituteBuilder retryIntervalMs(Long retryIntervalMs) { config.retryIntervalMs = retryIntervalMs; return this; }
        public ReconstituteBuilder rateLimitConfig(RateLimitConfig rateLimitConfig) { config.rateLimitConfig = rateLimitConfig; return this; }
        public ReconstituteBuilder maxQueueSize(Integer maxQueueSize) { config.maxQueueSize = maxQueueSize; return this; }
        public ReconstituteBuilder filterRules(FilterRules filterRules) { config.filterRules = filterRules; return this; }
        public ReconstituteBuilder pluginConfig(PluginConfig pluginConfig) { config.pluginConfig = pluginConfig; return this; }
        public ReconstituteBuilder receiptConfig(ReceiptConfig receiptConfig) { config.receiptConfig = receiptConfig; return this; }
        public ReconstituteBuilder extraConfig(ExtraConfig extraConfig) { config.extraConfig = extraConfig; return this; }
        public ReconstituteBuilder createTimeMs(Long createTimeMs) { config.createTimeMs = createTimeMs; return this; }
        public ReconstituteBuilder updateTimeMs(Long updateTimeMs) { config.updateTimeMs = updateTimeMs; return this; }
        public ReconstituteBuilder createOperator(String createOperator) { config.createOperator = createOperator; return this; }
        public ReconstituteBuilder updateOperator(String updateOperator) { config.updateOperator = updateOperator; return this; }
        public ReconstituteBuilder deleted(Boolean deleted) { config.deleted = deleted; return this; }
        public ReconstituteBuilder version(Integer version) { config.version = version; return this; }

        public ApiConfig build() { return config; }
    }

    public void update(UpdateApiConfigCommand command) {
        if (command.getGroupNo() != null) {
            this.groupNo = command.getGroupNo();
        }
        if (StrUtil.isNotBlank(command.getApiName())) {
            validateApiName(command.getApiName());
            this.apiName = command.getApiName();
        }
        if (command.getApiDescription() != null) {
            this.apiDescription = command.getApiDescription();
        }
        if (command.getStatus() != null) {
            this.status = command.getStatus();
        }
        if (command.getRequestTimeoutMs() != null) {
            this.requestTimeoutMs = command.getRequestTimeoutMs();
        }
        if (command.getAutoRetryCount() != null) {
            this.autoRetryCount = command.getAutoRetryCount();
        }
        if (command.getRetryIntervalMs() != null) {
            this.retryIntervalMs = command.getRetryIntervalMs();
        }
        if (command.getMaxQueueSize() != null) {
            this.maxQueueSize = command.getMaxQueueSize();
        }
        if (command.getRateLimitConfig() != null) {
            this.rateLimitConfig = command.getRateLimitConfig();
        }
        if (command.getFilterRules() != null) {
            this.filterRules = command.getFilterRules();
        }
        if (command.getPluginConfig() != null) {
            this.pluginConfig = command.getPluginConfig();
        }
        if (command.getReceiptConfig() != null) {
            this.receiptConfig = command.getReceiptConfig();
        }
        if (command.getExtraConfig() != null) {
            this.extraConfig = command.getExtraConfig();
        }
        this.updateOperator = command.getOperator();
        this.updateTimeMs = System.currentTimeMillis();

        registerEvent(new ApiConfigDomainEvent.Updated(
                this.apiCode, this.apiName, command.getOperator()
        ));
    }

    public void enable(String operator) {
        this.status = EnableStatus.ENABLED.getValue();
        this.updateOperator = operator;
        this.updateTimeMs = System.currentTimeMillis();

        registerEvent(new ApiConfigDomainEvent.Enabled(this.apiCode, operator));
    }

    public void disable(String operator) {
        this.status = EnableStatus.DISABLED.getValue();
        this.updateOperator = operator;
        this.updateTimeMs = System.currentTimeMillis();

        registerEvent(new ApiConfigDomainEvent.Disabled(this.apiCode, operator));
    }

    public void delete(DeleteApiConfigCommand command) {
        this.deleted = true;
        this.updateOperator = command.getOperator();
        this.updateTimeMs = System.currentTimeMillis();

        registerEvent(new ApiConfigDomainEvent.Deleted(this.apiCode, command.getOperator()));
    }

    public boolean isEnabled() {
        return EnableStatus.ENABLED.getValue().equals(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiConfig other)) return false;
        return apiCode != null && apiCode.equals(other.apiCode);
    }

    @Override
    public int hashCode() {
        return apiCode != null ? apiCode.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ApiConfig{id=" + id + ", apiCode='" + apiCode + "', apiName='" + apiName + "'}";
    }

    private static void validateApiCode(String apiCode) {
        if (StrUtil.isBlank(apiCode)) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "API编码不能为空");
        }
        if (apiCode.length() > 64) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "API编码长度不能超过64");
        }
    }

    private static void validateApiName(String apiName) {
        if (StrUtil.isBlank(apiName)) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "API名称不能为空");
        }
        if (apiName.length() > 128) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "API名称长度不能超过128");
        }
    }
}
