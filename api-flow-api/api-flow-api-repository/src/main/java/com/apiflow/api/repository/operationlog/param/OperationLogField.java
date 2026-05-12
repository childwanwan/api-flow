package com.apiflow.api.repository.operationlog.param;

import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperationLogField implements FieldMetadata {
    ID("id", "id", null),
    BIZ_CODE("bizCode", "biz_code", null),
    LOG_TYPE("logType", "log_type", null),
    LOG_DATA("logData", "log_data", null),
    OPERATOR("operator", "operator", null),
    OPERATE_TIME_MS("operateTimeMs", "operate_time_ms", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null),

    SHOW_DETAIL("showDetail", "log_data", "showDetail"),

    ;

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
