package com.apiflow.infrastructure.persistence.mybatis.config.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_config")
public class ApiConfigPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("group_no")
    private String groupNo;

    @TableField("api_code")
    private String apiCode;

    @TableField("api_name")
    private String apiName;

    @TableField("api_description")
    private String apiDescription;

    @TableField("status")
    private String status;

    @TableField("request_timeout_ms")
    private Long requestTimeoutMs;

    @TableField("auto_retry_count")
    private Integer autoRetryCount;

    @TableField("retry_interval_ms")
    private Long retryIntervalMs;

    @TableField("rate_limit_config")
    private String rateLimitConfig;

    @TableField("max_queue_size")
    private Integer maxQueueSize;

    @TableField("filter_rules")
    private String filterRules;

    @TableField("plugin_config")
    private String pluginConfig;

    @TableField("receipt_config")
    private String receiptConfig;

    @TableField("extra_config")
    private String extraConfig;

    @TableField("create_time_ms")
    private Long createTimeMs;

    @TableField("update_time_ms")
    private Long updateTimeMs;

    @TableField("create_operator")
    private String createOperator;

    @TableField("update_operator")
    private String updateOperator;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @TableField("version")
    @Version
    private Integer version;

}
