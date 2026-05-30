package com.apiflow.application.alarm.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAlarmRuleParam {
    private Long id;
    private String ruleName;
    private String alarmType;
    private String triggerCondition;
    private String level;
    private Boolean enabled;
}
