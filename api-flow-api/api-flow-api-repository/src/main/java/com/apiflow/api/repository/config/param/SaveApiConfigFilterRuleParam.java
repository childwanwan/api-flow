package com.apiflow.api.repository.config.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveApiConfigFilterRuleParam {
    private String field;
    private String operator;
    private Object value;
    private String message;
}
