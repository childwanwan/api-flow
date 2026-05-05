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
public class ApiConfigRateLimitRuleRequest {

    private String name;
    private Long maxRequests;
    private Long timeWindowMs;

    public void validate() {
        ValidationHelper.validateNotBlank(name, "name");
        ValidationHelper.validateSize(name, 64, "name");
        ValidationHelper.validateNotNull(maxRequests, "maxRequests");
        ValidationHelper.validateRange(maxRequests, 1L, 1000000L, "maxRequests");
        ValidationHelper.validateNotNull(timeWindowMs, "timeWindowMs");
        ValidationHelper.validateRange(timeWindowMs, 1000L, 3600000L, "timeWindowMs");
    }
}
