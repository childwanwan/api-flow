package com.apiflow.api.repository.user.param;

import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserField implements FieldMetadata {
    ID("id", "id", null),
    USERNAME("username", "username", null),
    PASSWORD("password", "password", null),
    ROLE("role", "role", null),
    STATUS("status", "status", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null),
    UPDATE_TIME_MS("updateTimeMs", "update_time_ms", null),
    CREATE_OPERATOR("createOperator", "create_operator", null),
    LAST_LOGIN_TIME_MS("lastLoginTimeMs", "last_login_time_ms", null);

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
