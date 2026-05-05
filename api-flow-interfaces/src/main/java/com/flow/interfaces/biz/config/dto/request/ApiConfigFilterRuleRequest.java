package com.flow.interfaces.biz.config.dto.request;

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
public class ApiConfigFilterRuleRequest {

    private String name;
    private String type;
    private String expression;
    private Boolean enabled;

    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "name cannot be blank");
        }
        if (name != null && name.length() > 64) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "name length must be less than or equal to 64");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "type cannot be blank");
        }
        if (type != null && type.length() > 32) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "type length must be less than or equal to 32");
        }
        if (expression == null || expression.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "expression cannot be blank");
        }
        if (expression != null && expression.length() > 1024) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "expression length must be less than or equal to 1024");
        }
    }
}