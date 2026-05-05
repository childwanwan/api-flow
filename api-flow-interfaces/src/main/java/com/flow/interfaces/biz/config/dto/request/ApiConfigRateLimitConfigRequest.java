package com.flow.interfaces.biz.config.dto.request;

import com.flow.common.exception.BusinessException;
import com.flow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigRateLimitConfigRequest {

    private Boolean enabled;
    private List<ApiConfigRateLimitRuleRequest> rules;

    public void validate() {
        if (rules != null && rules.size() > 10) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "rate limit rules count must be less than or equal to 10");
        }
        if (rules != null) {
            for (int i = 0; i < rules.size(); i++) {
                rules.get(i).validate();
            }
        }
    }
}