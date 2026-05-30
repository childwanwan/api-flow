package com.apiflow.interfaces.biz.alarm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmRuleVO {
    private Long id;
    private String ruleName;
    private String alarmType;
    private String triggerCondition;
    private String level;
    private Boolean enabled;
    private Long createTimeMs;
    private Long updateTimeMs;
}
