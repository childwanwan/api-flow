package com.flow.api.repository.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigFilterRuleIDTO {
    private String field;
    private String operator;
    private Object value;
    private String message;
}
