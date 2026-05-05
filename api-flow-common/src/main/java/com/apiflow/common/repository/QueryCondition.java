package com.apiflow.common.repository;

import java.util.List;

public class QueryCondition<F extends Enum<F> & FieldMetadata> {

    private final F field;
    private final MatchType matchType;
    private final Object value;
    private final List<Object> values;

    private QueryCondition(F field, MatchType matchType, Object value, List<Object> values) {
        this.field = field;
        this.matchType = matchType;
        this.value = value;
        this.values = values;
    }

    public F getField() {
        return field;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public Object getValue() {
        return value;
    }

    public List<Object> getValues() {
        return values;
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> eq(F field, Object value) {
        return new QueryCondition<>(field, MatchType.EQ, value, null);
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> ne(F field, Object value) {
        return new QueryCondition<>(field, MatchType.NE, value, null);
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> gt(F field, Object value) {
        return new QueryCondition<>(field, MatchType.GT, value, null);
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> ge(F field, Object value) {
        return new QueryCondition<>(field, MatchType.GE, value, null);
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> lt(F field, Object value) {
        return new QueryCondition<>(field, MatchType.LT, value, null);
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> le(F field, Object value) {
        return new QueryCondition<>(field, MatchType.LE, value, null);
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> like(F field, Object value) {
        return new QueryCondition<>(field, MatchType.LIKE, value, null);
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> in(F field, List<Object> values) {
        return new QueryCondition<>(field, MatchType.IN, null, values);
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> notIn(F field, List<Object> values) {
        return new QueryCondition<>(field, MatchType.NOT_IN, null, values);
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> between(F field, Object start, Object end) {
        return new QueryCondition<>(field, MatchType.BETWEEN, null, List.of(start, end));
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> isNull(F field) {
        return new QueryCondition<>(field, MatchType.IS_NULL, null, null);
    }

    public static <F extends Enum<F> & FieldMetadata> QueryCondition<F> isNotNull(F field) {
        return new QueryCondition<>(field, MatchType.IS_NOT_NULL, null, null);
    }
}
