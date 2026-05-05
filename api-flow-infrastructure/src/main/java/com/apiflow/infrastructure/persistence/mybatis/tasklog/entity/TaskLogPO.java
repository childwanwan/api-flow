package com.apiflow.infrastructure.persistence.mybatis.tasklog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_task_log")
public class TaskLogPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("task_no")
    private String taskNo;

    @TableField("log_type")
    private String logType;

    @TableField("log_data")
    private String logData;

    @TableField("create_time_ms")
    private Long createTimeMs;
}
