package com.apiflow.api.repository.alarm.param;

import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmRuleField implements FieldMetadata {
    ID("id", "id", null),
    RULE_NAME("ruleName", "rule_name", null),
    ALARM_TYPE("alarmType", "alarm_type", null),
    TRIGGER_CONDITION("triggerCondition", "trigger_condition", null),
    LEVEL("level", "level", null),
    ENABLED("enabled", "enabled", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null),
    UPDATE_TIME_MS("updateTimeMs", "update_time_ms", null);

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
