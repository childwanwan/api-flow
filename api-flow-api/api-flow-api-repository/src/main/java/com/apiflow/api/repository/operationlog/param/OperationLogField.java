package com.apiflow.api.repository.operationlog.param;

import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperationLogField implements FieldMetadata {
    ID("id", "id", null),
    USERNAME("username", "username", null),
    OPERATION("operation", "operation", null),
    MODULE("module", "module", null),
    DETAIL("detail", "detail", null),
    IP("ip", "ip", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null);

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
