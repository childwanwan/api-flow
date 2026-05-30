package com.apiflow.domain.config.model;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class FilterRules {

    private final List<FilterRule> rules;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterRules other)) return false;
        return ObjectUtil.equal(rules, other.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rules);
    }
}
