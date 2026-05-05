package com.apiflow.common.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchType {
    EQ,
    NE,
    GT,
    GE,
    LT,
    LE,
    LIKE,
    IN,
    NOT_IN,
    BETWEEN,
    IS_NULL,
    IS_NOT_NULL;
}
