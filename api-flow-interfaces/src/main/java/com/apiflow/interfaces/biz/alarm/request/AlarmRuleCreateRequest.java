package com.apiflow.interfaces.biz.alarm.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmRuleCreateRequest {
    private String ruleName;
    private String alarmType;
    private String triggerCondition;
    private String level;
    private Boolean enabled;
}
