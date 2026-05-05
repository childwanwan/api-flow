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
public class ApiConfigFilterRulesRequest {

    private List<ApiConfigFilterRuleRequest> rules;

    public void validate() {
        if (rules != null && rules.size() > 20) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "filter rules count must be less than or equal to 20");
        }
        if (rules != null) {
            for (int i = 0; i < rules.size(); i++) {
                rules.get(i).validate();
            }
        }
    }
}