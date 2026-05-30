package com.apiflow.domain.config.model;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class RateLimitRule {

    private final String name;
    private final String type;
    private final String dimension;
    private final String keyTemplate;
    private final Integer limit;
    private final Integer windowSeconds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitRule other)) return false;
        return ObjectUtil.equal(name, other.name)
                && ObjectUtil.equal(type, other.type)
                && ObjectUtil.equal(dimension, other.dimension)
                && ObjectUtil.equal(keyTemplate, other.keyTemplate)
                && ObjectUtil.equal(limit, other.limit)
                && ObjectUtil.equal(windowSeconds, other.windowSeconds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, dimension, keyTemplate, limit, windowSeconds);
    }
}
