package com.apiflow.domain.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateLimitConfig {

    private Boolean enabled;
    private List<RateLimitRule> rules;
}
