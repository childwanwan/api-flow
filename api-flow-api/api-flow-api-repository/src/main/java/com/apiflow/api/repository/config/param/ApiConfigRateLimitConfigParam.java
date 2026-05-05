package com.apiflow.api.repository.config.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigRateLimitConfigParam {
    private Boolean enabled;
    private List<ApiConfigRateLimitRuleParam> rules;
}
