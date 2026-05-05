package com.flow.interfaces.biz.task.dto;

import com.flow.api.repository.task.idto.TaskReceiptConfigIDTO;
import com.flow.common.exception.BusinessException;
import com.flow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskSubmitRequest {

    private String apiCode;
    private String source;
    private String groupNo;
    private String actionType;
    private Integer priority;
    private Map<String, Object> params;
    private Map<String, Object> customData;
    private TaskReceiptConfigIDTO receiptConfig;

    public void validate() {
        validateNotBlank(apiCode, "apiCode");
        validateSize(apiCode, 128, "apiCode");
        validatePattern(apiCode, "^[a-zA-Z0-9_-]+$", "apiCode");
        validateNotBlank(source, "source");
        validateSize(source, 64, "source");
        validateSize(groupNo, 64, "groupNo");
        validateSize(actionType, 32, "actionType");
        validateRange(priority, 1, 10, "priority");
        validateNotNull(params, "params");
        validateMapSize(params, 1000, "params");
        validateMapSize(customData, 100, "customData");
    }

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " cannot be blank");
        }
    }

    private void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " cannot be null");
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

    private void validateRange(Integer value, Integer min, Integer max, String fieldName) {
        if (value != null && (value < min || value > max)) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " must be between " + min + " and " + max);
        }
    }

    private void validateMapSize(Map<?, ?> map, int max, String fieldName) {
        if (map != null && map.size() > max) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " size must be less than or equal to " + max);
        }
    }
}