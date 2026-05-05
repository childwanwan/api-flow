package com.apiflow.domain.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterRule {

    private String field;
    private String operator;
    private String value;
    private String message;
}
