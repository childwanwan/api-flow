package com.apiflow.domain.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateLimitRule {

    private String name;
    private String type;
    private String dimension;
    private String keyTemplate;
    private Integer limit;
    private Integer windowSeconds;
}
