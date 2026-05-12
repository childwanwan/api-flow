package com.apiflow.infrastructure.persistence.mybatis.alarm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_alarm_record")
public class AlarmRecordPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("event_type")
    private String eventType;

    @TableField("level")
    private String level;

    @TableField("message")
    private String message;

    @TableField("detail")
    private String detail;

    @TableField("task_no")
    private String taskNo;

    @TableField("api_code")
    private String apiCode;

    @TableField("create_time_ms")
    private Long createTimeMs;
}
