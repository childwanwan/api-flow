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
public class ApiConfigRateLimitRuleRequest {

    private String name;
    private Long maxRequests;
    private Long timeWindowMs;

    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "name cannot be blank");
        }
        if (name != null && name.length() > 64) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "name length must be less than or equal to 64");
        }
        if (maxRequests == null) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "maxRequests cannot be null");
        }
        if (maxRequests < 1 || maxRequests > 1000000) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "maxRequests must be between 1 and 1000000");
        }
        if (timeWindowMs == null) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "timeWindowMs cannot be null");
        }
        if (timeWindowMs < 1000 || timeWindowMs > 3600000) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "timeWindowMs must be between 1000 and 3600000");
        }
    }
}