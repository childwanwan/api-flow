package com.apiflow.api.repository.alarm.param;

import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmRecordField implements FieldMetadata {
    ID("id", "id", null),
    EVENT_TYPE("eventType", "event_type", null),
    LEVEL("level", "level", null),
    MESSAGE("message", "message", null),
    DETAIL("detail", "detail", null),
    TASK_NO("taskNo", "task_no", null),
    API_CODE("apiCode", "api_code", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null);

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
