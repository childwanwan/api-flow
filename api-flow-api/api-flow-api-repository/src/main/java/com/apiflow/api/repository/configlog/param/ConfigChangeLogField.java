package com.apiflow.api.repository.configlog.param;

import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfigChangeLogField implements FieldMetadata {
    ID("id", "id", null),
    API_CODE("apiCode", "api_code", null),
    CHANGE_TYPE("changeType", "change_type", null),
    BEFORE_CONFIG("beforeConfig", "before_config", null),
    AFTER_CONFIG("afterConfig", "after_config", null),
    OPERATOR("operator", "operator", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null);

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
