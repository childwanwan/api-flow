package com.apiflow.interfaces.biz.config.request;

import com.apiflow.interfaces.util.ValidationHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigFilterRuleRequest {

    private String field;
    private String operator;
    private String value;
    private String message;

    public void validate() {
        ValidationHelper.validateNotBlank(field, "field");
        ValidationHelper.validateSize(field, 64, "field");
        ValidationHelper.validateNotBlank(operator, "operator");
        ValidationHelper.validateSize(operator, 32, "operator");
        ValidationHelper.validateNotBlank(value, "value");
        ValidationHelper.validateSize(value, 1024, "value");
        ValidationHelper.validateSize(message, 512, "message");
    }
}
