package com.apiflow.domain.config.model;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class RateLimitConfig {

    private final Boolean enabled;
    private final List<RateLimitRule> rules;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitConfig other)) return false;
        return ObjectUtil.equal(enabled, other.enabled) && ObjectUtil.equal(rules, other.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, rules);
    }
}
