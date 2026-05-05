package com.apiflow.infrastructure.persistence.mybatis.task.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_task")
public class TaskPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("task_no")
    private String taskNo;

    @TableField("source")
    private String source;

    @TableField("group_no")
    private String groupNo;

    @TableField("api_code")
    private String apiCode;

    @TableField("api_name")
    private String apiName;

    @TableField("action_type")
    private String actionType;

    @TableField("status")
    private String status;

    @TableField("interrupt_flag")
    private Boolean interruptFlag;

    @TableField("compensate_status")
    private String compensateStatus;

    @TableField("compensate_retry_count")
    private Integer compensateRetryCount;

    @TableField("compensate_next_retry_time_ms")
    private Long compensateNextRetryTimeMs;

    @TableField("priority")
    private Integer priority;

    @TableField("request_context")
    private String requestContext;

    @TableField("exec_info")
    private String execInfo;

    @TableField("response_data")
    private String responseData;

    @TableField("expire_time_ms")
    private Long expireTimeMs;

    @TableField("receipt_config")
    private String receiptConfig;

    @TableField("receipt_info")
    private String receiptInfo;

    @TableField("retry_count")
    private Integer retryCount;

    @TableField("max_retry_count")
    private Integer maxRetryCount;

    @TableField("next_retry_time_ms")
    private Long nextRetryTimeMs;

    @TableField("create_time_ms")
    private Long createTimeMs;

    @TableField("start_execute_time_ms")
    private Long startExecuteTimeMs;

    @TableField("end_execute_time_ms")
    private Long endExecuteTimeMs;

    @TableField("execute_duration_ms")
    private Long executeDurationMs;

    @TableField("cancel_time_ms")
    private Long cancelTimeMs;

    @TableField("cancel_reason")
    private String cancelReason;

    @TableField("canceled_by")
    private String canceledBy;

    @TableField("update_time_ms")
    private Long updateTimeMs;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @TableField("version")
    @Version
    private Integer version;

}
