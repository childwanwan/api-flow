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
@TableName("sys_alarm_rule")
public class AlarmRulePO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("rule_name")
    private String ruleName;

    @TableField("alarm_type")
    private String alarmType;

    @TableField("trigger_condition")
    private String triggerCondition;

    @TableField("level")
    private String level;

    @TableField("enabled")
    private Boolean enabled;

    @TableField("create_time_ms")
    private Long createTimeMs;

    @TableField("update_time_ms")
    private Long updateTimeMs;
}
