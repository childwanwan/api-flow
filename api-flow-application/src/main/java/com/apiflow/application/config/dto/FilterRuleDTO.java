package com.apiflow.application.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterRuleDTO {

    private String field;
    private String operator;
    private String value;
    private String message;
}
