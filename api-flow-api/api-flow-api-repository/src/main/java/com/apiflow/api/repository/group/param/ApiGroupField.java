package com.apiflow.api.repository.group.param;

import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiGroupField implements FieldMetadata {
    ID("id", "id", null),
    GROUP_NO("groupNo", "group_no", null),
    GROUP_NAME("groupName", "group_name", null),
    GROUP_DESCRIPTION("groupDescription", "group_description", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null),
    UPDATE_TIME_MS("updateTimeMs", "update_time_ms", null),
    CREATE_OPERATOR("createOperator", "create_operator", null),
    UPDATE_OPERATOR("updateOperator", "update_operator", null),
    DELETED("deleted", "deleted", null),
    VERSION("version", "version", null);

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
