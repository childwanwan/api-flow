package com.apiflow.infrastructure.persistence.mybatis.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.common.repository.FieldMetadata;
import com.apiflow.common.repository.MatchType;
import com.apiflow.common.repository.QueryCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QueryConditionHelper {

    public static <T, V> void applyFieldCondition(
            LambdaQueryWrapper<T> wrapper,
            SFunction<T, ?> column,
            FieldCondition<V> condition) {
        if (condition == null || !condition.hasAnyCondition()) {
            return;
        }
        if (condition.getEq() != null) {
            wrapper.eq(column, condition.getEq());
        }
        if (condition.getNe() != null) {
            wrapper.ne(column, condition.getNe());
        }
        if (condition.getGt() != null) {
            wrapper.gt(column, condition.getGt());
        }
        if (condition.getGe() != null) {
            wrapper.ge(column, condition.getGe());
        }
        if (condition.getLt() != null) {
            wrapper.lt(column, condition.getLt());
        }
        if (condition.getLe() != null) {
            wrapper.le(column, condition.getLe());
        }
        if (condition.getLike() != null) {
            wrapper.like(column, condition.getLike());
        }
        if (condition.getIn() != null && !condition.getIn().isEmpty()) {
            wrapper.in(column, condition.getIn());
        }
        if (condition.getNotIn() != null && !condition.getNotIn().isEmpty()) {
            wrapper.notIn(column, condition.getNotIn());
        }
        if (condition.getBetweenStart() != null && condition.getBetweenEnd() != null) {
            wrapper.between(column, condition.getBetweenStart(), condition.getBetweenEnd());
        }
        if (Boolean.TRUE.equals(condition.getIsNull())) {
            wrapper.isNull(column);
        }
        if (Boolean.TRUE.equals(condition.getIsNotNull())) {
            wrapper.isNotNull(column);
        }
    }

    public static <V> void applyFieldCondition(
            QueryWrapper<?> wrapper,
            String column,
            FieldCondition<V> condition) {
        if (condition == null || !condition.hasAnyCondition()) {
            return;
        }
        if (condition.getEq() != null) {
            wrapper.eq(column, condition.getEq());
        }
        if (condition.getNe() != null) {
            wrapper.ne(column, condition.getNe());
        }
        if (condition.getGt() != null) {
            wrapper.gt(column, condition.getGt());
        }
        if (condition.getGe() != null) {
            wrapper.ge(column, condition.getGe());
        }
        if (condition.getLt() != null) {
            wrapper.lt(column, condition.getLt());
        }
        if (condition.getLe() != null) {
            wrapper.le(column, condition.getLe());
        }
        if (condition.getLike() != null) {
            wrapper.like(column, condition.getLike());
        }
        if (condition.getIn() != null && !condition.getIn().isEmpty()) {
            wrapper.in(column, condition.getIn());
        }
        if (condition.getNotIn() != null && !condition.getNotIn().isEmpty()) {
            wrapper.notIn(column, condition.getNotIn());
        }
        if (condition.getBetweenStart() != null && condition.getBetweenEnd() != null) {
            wrapper.between(column, condition.getBetweenStart(), condition.getBetweenEnd());
        }
        if (Boolean.TRUE.equals(condition.getIsNull())) {
            wrapper.isNull(column);
        }
        if (Boolean.TRUE.equals(condition.getIsNotNull())) {
            wrapper.isNotNull(column);
        }
    }

    public static <T, V> void applyJsonFieldCondition(
            LambdaQueryWrapper<T> wrapper,
            String jsonColumn,
            String jsonPath,
            FieldCondition<V> condition) {
        applyJsonFieldConditionImpl(wrapper, jsonColumn, jsonPath, condition);
    }

    public static <V> void applyJsonFieldCondition(
            QueryWrapper<?> wrapper,
            String jsonColumn,
            String jsonPath,
            FieldCondition<V> condition) {
        applyJsonFieldConditionImpl(wrapper, jsonColumn, jsonPath, condition);
    }

    private static <V> void applyJsonFieldConditionImpl(
            com.baomidou.mybatisplus.core.conditions.AbstractWrapper<?, ?, ?> wrapper,
            String jsonColumn,
            String jsonPath,
            FieldCondition<V> condition) {
        if (condition == null || !condition.hasAnyCondition()) {
            return;
        }
        String columnSql = String.format("JSON_UNQUOTE(JSON_EXTRACT(%s, '$.%s'))", jsonColumn, jsonPath);
        if (condition.getEq() != null) {
            wrapper.apply(columnSql + " = {0}", condition.getEq());
        }
        if (condition.getNe() != null) {
            wrapper.apply(columnSql + " != {0}", condition.getNe());
        }
        if (condition.getGt() != null) {
            wrapper.apply(columnSql + " > {0}", condition.getGt());
        }
        if (condition.getGe() != null) {
            wrapper.apply(columnSql + " >= {0}", condition.getGe());
        }
        if (condition.getLt() != null) {
            wrapper.apply(columnSql + " < {0}", condition.getLt());
        }
        if (condition.getLe() != null) {
            wrapper.apply(columnSql + " <= {0}", condition.getLe());
        }
        if (condition.getLike() != null) {
            wrapper.apply(columnSql + " LIKE {0}", "%" + condition.getLike() + "%");
        }
        if (condition.getIn() != null && !condition.getIn().isEmpty()) {
            String placeholders = IntStream.range(0, condition.getIn().size())
                    .mapToObj(i -> "{" + i + "}")
                    .collect(Collectors.joining(", "));
            wrapper.apply(columnSql + " IN (" + placeholders + ")", condition.getIn().toArray());
        }
        if (condition.getNotIn() != null && !condition.getNotIn().isEmpty()) {
            String placeholders = IntStream.range(0, condition.getNotIn().size())
                    .mapToObj(i -> "{" + i + "}")
                    .collect(Collectors.joining(", "));
            wrapper.apply(columnSql + " NOT IN (" + placeholders + ")", condition.getNotIn().toArray());
        }
        if (condition.getBetweenStart() != null && condition.getBetweenEnd() != null) {
            wrapper.apply(columnSql + " BETWEEN {0} AND {1}", condition.getBetweenStart(), condition.getBetweenEnd());
        }
        if (Boolean.TRUE.equals(condition.getIsNull())) {
            wrapper.apply(columnSql + " IS NULL");
        }
        if (Boolean.TRUE.equals(condition.getIsNotNull())) {
            wrapper.apply(columnSql + " IS NOT NULL");
        }
    }

    public static <F extends Enum<F> & FieldMetadata> void applyConditions(
            QueryWrapper<?> wrapper, List<QueryCondition<F>> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return;
        }
        for (QueryCondition<F> condition : conditions) {
            applyConditionImpl(wrapper, condition);
        }
    }

    public static <F extends Enum<F> & FieldMetadata> void applyConditions(
            LambdaQueryWrapper<?> wrapper, List<QueryCondition<F>> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return;
        }
        for (QueryCondition<F> condition : conditions) {
            applyConditionImpl(wrapper, condition);
        }
    }

    private static <F extends Enum<F> & FieldMetadata> void applyConditionImpl(
            com.baomidou.mybatisplus.core.conditions.AbstractWrapper<?, ?, ?> wrapper, QueryCondition<F> condition) {
        FieldMetadata field = condition.getField();
        String columnSql = resolveColumnSql(field);
        MatchType matchType = condition.getMatchType();

        switch (matchType) {
            case EQ:
                wrapper.apply(columnSql + " = {0}", condition.getValue());
                break;
            case NE:
                wrapper.apply(columnSql + " != {0}", condition.getValue());
                break;
            case GT:
                wrapper.apply(columnSql + " > {0}", condition.getValue());
                break;
            case GE:
                wrapper.apply(columnSql + " >= {0}", condition.getValue());
                break;
            case LT:
                wrapper.apply(columnSql + " < {0}", condition.getValue());
                break;
            case LE:
                wrapper.apply(columnSql + " <= {0}", condition.getValue());
                break;
            case LIKE:
                wrapper.apply(columnSql + " LIKE {0}", "%" + condition.getValue() + "%");
                break;
            case IN:
                if (condition.getValues() != null && !condition.getValues().isEmpty()) {
                    String placeholders = IntStream.range(0, condition.getValues().size())
                            .mapToObj(i -> "{" + i + "}")
                            .collect(Collectors.joining(", "));
                    wrapper.apply(columnSql + " IN (" + placeholders + ")", condition.getValues().toArray());
                }
                break;
            case NOT_IN:
                if (condition.getValues() != null && !condition.getValues().isEmpty()) {
                    String placeholders = IntStream.range(0, condition.getValues().size())
                            .mapToObj(i -> "{" + i + "}")
                            .collect(Collectors.joining(", "));
                    wrapper.apply(columnSql + " NOT IN (" + placeholders + ")", condition.getValues().toArray());
                }
                break;
            case BETWEEN:
                wrapper.apply(columnSql + " BETWEEN {0} AND {1}", condition.getValues().get(0), condition.getValues().get(1));
                break;
            case IS_NULL:
                wrapper.apply(columnSql + " IS NULL");
                break;
            case IS_NOT_NULL:
                wrapper.apply(columnSql + " IS NOT NULL");
                break;
            default:
                break;
        }
    }

    public static void applySelectFields(QueryWrapper<?> wrapper, List<? extends FieldMetadata> selectFields) {
        if (selectFields == null || selectFields.isEmpty()) {
            return;
        }
        List<String> columns = new ArrayList<>();
        for (FieldMetadata field : selectFields) {
            if (field.isJsonField()) {
                String alias = field.getFieldName().replace(".", "_");
                columns.add(String.format("JSON_UNQUOTE(JSON_EXTRACT(%s, '$.%s')) AS %s",
                        field.getColumnName(), field.getJsonPath(), alias));
            } else {
                columns.add(field.getColumnName());
            }
        }
        wrapper.select(columns.toArray(new String[0]));
    }

    private static String resolveColumnSql(FieldMetadata field) {
        if (field.isJsonField()) {
            return String.format("JSON_UNQUOTE(JSON_EXTRACT(%s, '$.%s'))",
                    field.getColumnName(), field.getJsonPath());
        }
        return field.getColumnName();
    }
}
