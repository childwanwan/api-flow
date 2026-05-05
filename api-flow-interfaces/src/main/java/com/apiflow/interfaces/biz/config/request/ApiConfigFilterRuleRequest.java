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

    private String name;
    private String type;
    private String expression;
    private Boolean enabled;

    public void validate() {
        ValidationHelper.validateNotBlank(name, "name");
        ValidationHelper.validateSize(name, 64, "name");
        ValidationHelper.validateNotBlank(type, "type");
        ValidationHelper.validateSize(type, 32, "type");
        ValidationHelper.validateNotBlank(expression, "expression");
        ValidationHelper.validateSize(expression, 1024, "expression");
    }
}
