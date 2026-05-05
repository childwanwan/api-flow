package com.flow.interfaces.biz.task.dto;

import com.flow.common.exception.BusinessException;
import com.flow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRetryRequest {

    private String taskNo;
    private String retryOperator;

    public void validate() {
        validateNotBlank(taskNo, "taskNo");
        validateSize(taskNo, 64, "taskNo");
        validatePattern(taskNo, "^[a-zA-Z0-9_-]+$", "taskNo");
        validateSize(retryOperator, 64, "retryOperator");
    }

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " cannot be blank");
        }
    }

    private void validateSize(String value, int max, String fieldName) {
        if (value != null && value.length() > max) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " length must be less than or equal to " + max);
        }
    }

    private void validatePattern(String value, String regex, String fieldName) {
        if (value != null && !value.matches(regex)) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " can only contain letters, numbers, underscore and hyphen");
        }
    }
}