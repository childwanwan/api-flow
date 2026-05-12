package com.apiflow.common.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogicType {
    AND("AND"),
    OR("OR"),
    NOT("NOT");

    private final String sql;
}
