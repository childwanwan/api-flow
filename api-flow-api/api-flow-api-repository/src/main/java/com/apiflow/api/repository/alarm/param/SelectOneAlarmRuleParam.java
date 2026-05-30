package com.apiflow.api.repository.alarm.param;

import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.common.repository.QueryCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectOneAlarmRuleParam {
    private FieldCondition<String> ruleName;
    private FieldCondition<String> alarmType;
    private FieldCondition<Boolean> enabled;
    private List<AlarmRuleField> selectFields;
    private List<QueryCondition<AlarmRuleField>> conditions;
    private ConditionNode condition;

    public boolean isEmpty() {
        return !(ruleName != null && ruleName.hasAnyCondition())
                && !(alarmType != null && alarmType.hasAnyCondition())
                && !(enabled != null && enabled.hasAnyCondition())
                && (conditions == null || conditions.isEmpty())
                && (condition == null || condition.isEmpty());
    }
}
