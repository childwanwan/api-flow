package com.apiflow.interfaces.biz.config.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigFilterRuleVO {
    private String name;
    private String type;
    private String expression;
    private Boolean enabled;
}
