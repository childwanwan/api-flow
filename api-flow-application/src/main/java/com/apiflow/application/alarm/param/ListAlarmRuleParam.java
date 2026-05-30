package com.apiflow.application.alarm.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListAlarmRuleParam {
    private String alarmType;
    private Boolean enabled;
}
