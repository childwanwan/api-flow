package com.apiflow.interfaces.biz.config.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigRateLimitRuleVO {
    private String name;
    private Long maxRequests;
    private Long timeWindowMs;
}
