package com.apiflow.api.repository.alarm.param;

import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.repository.FieldCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectAlarmRuleParam {
    private FieldCondition<String> alarmType;
    private FieldCondition<Boolean> enabled;
    private Integer limit;
    private ConditionNode condition;
}
