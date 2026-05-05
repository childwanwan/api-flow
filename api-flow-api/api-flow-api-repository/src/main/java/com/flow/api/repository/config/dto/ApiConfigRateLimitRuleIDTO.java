package com.flow.api.repository.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigRateLimitRuleIDTO {
    private String name;
    private String type;
    private String dimension;
    private String keyTemplate;
    private Integer limit;
    private Integer windowSeconds;
}
