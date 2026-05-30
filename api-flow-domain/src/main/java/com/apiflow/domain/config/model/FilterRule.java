package com.apiflow.domain.config.model;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class FilterRule {

    private final String field;
    private final String operator;
    private final String value;
    private final String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterRule other)) return false;
        return ObjectUtil.equal(field, other.field)
                && ObjectUtil.equal(operator, other.operator)
                && ObjectUtil.equal(value, other.value)
                && ObjectUtil.equal(message, other.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, operator, value, message);
    }
}
