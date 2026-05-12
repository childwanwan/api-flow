package com.apiflow.api.repository.alarm.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmRuleIDTO {
    private Long id;
    private String ruleName;
    private String alarmType;
    private String triggerCondition;
    private String level;
    private Boolean enabled;
    private Long createTimeMs;
    private Long updateTimeMs;
}
