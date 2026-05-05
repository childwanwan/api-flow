package com.apiflow.api.repository.tasklog.param;

import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskLogField implements FieldMetadata {
    ID("id", "id", null),
    TASK_NO("taskNo", "task_no", null),
    LOG_TYPE("logType", "log_type", null),
    LOG_DATA("logData", "log_data", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null);

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
