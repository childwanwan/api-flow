package com.apiflow.api.repository.task.param;

import com.apiflow.common.repository.FieldMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskField implements FieldMetadata {
    ID("id", "id", null),
    TASK_NO("taskNo", "task_no", null),
    SOURCE("source", "source", null),
    GROUP_NO("groupNo", "group_no", null),
    API_CODE("apiCode", "api_code", null),
    API_NAME("apiName", "api_name", null),
    ACTION_TYPE("actionType", "action_type", null),
    STATUS("status", "status", null),
    INTERRUPT_FLAG("interruptFlag", "interrupt_flag", null),
    COMPENSATE_STATUS("compensateStatus", "compensate_status", null),
    COMPENSATE_RETRY_COUNT("compensateRetryCount", "compensate_retry_count", null),
    COMPENSATE_NEXT_RETRY_TIME_MS("compensateNextRetryTimeMs", "compensate_next_retry_time_ms", null),
    PRIORITY("priority", "priority", null),
    EXPIRE_TIME_MS("expireTimeMs", "expire_time_ms", null),
    RETRY_COUNT("retryCount", "retry_count", null),
    MAX_RETRY_COUNT("maxRetryCount", "max_retry_count", null),
    NEXT_RETRY_TIME_MS("nextRetryTimeMs", "next_retry_time_ms", null),
    CREATE_TIME_MS("createTimeMs", "create_time_ms", null),
    START_EXECUTE_TIME_MS("startExecuteTimeMs", "start_execute_time_ms", null),
    END_EXECUTE_TIME_MS("endExecuteTimeMs", "end_execute_time_ms", null),
    EXECUTE_DURATION_MS("executeDurationMs", "execute_duration_ms", null),
    CANCEL_TIME_MS("cancelTimeMs", "cancel_time_ms", null),
    CANCEL_REASON("cancelReason", "cancel_reason", null),
    CANCELED_BY("canceledBy", "canceled_by", null),
    UPDATE_TIME_MS("updateTimeMs", "update_time_ms", null),
    DELETED("deleted", "deleted", null),
    VERSION("version", "version", null),
    REQUEST_CONTEXT("requestContext", "request_context", null),
    EXEC_INFO("execInfo", "exec_info", null),
    RESPONSE_DATA("responseData", "response_data", null),
    RECEIPT_CONFIG("receiptConfig", "receipt_config", null),
    RECEIPT_INFO("receiptInfo", "receipt_info", null),
    REQUEST_CONTEXT_TRACE_ID("requestContext.traceId", "request_context", "traceId"),
    EXEC_INFO_ATTEMPT_COUNT("execInfo.attemptCount", "exec_info", "attemptCount");

    private final String fieldName;
    private final String columnName;
    private final String jsonPath;

    @Override
    public boolean isJsonField() {
        return jsonPath != null;
    }
}
