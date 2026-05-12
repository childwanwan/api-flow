package com.apiflow.common.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConditionNode {

    private String field;
    private FieldCondition<?> fieldCondition;
    private LogicType logicType;
    private List<ConditionNode> children;

    public boolean isLeaf() {
        return field != null;
    }

    public boolean isEmpty() {
        if (isLeaf()) {
            return fieldCondition == null || !fieldCondition.hasAnyCondition();
        }
        return children == null || children.stream().allMatch(ConditionNode::isEmpty);
    }

    public static ConditionNode eq(String field, Object value) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().eq(value).build())
                .build();
    }

    public static ConditionNode ne(String field, Object value) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().ne(value).build())
                .build();
    }

    public static ConditionNode gt(String field, Object value) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().gt(value).build())
                .build();
    }

    public static ConditionNode ge(String field, Object value) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().ge(value).build())
                .build();
    }

    public static ConditionNode lt(String field, Object value) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().lt(value).build())
                .build();
    }

    public static ConditionNode le(String field, Object value) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().le(value).build())
                .build();
    }

    public static ConditionNode like(String field, String value) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().like(value).build())
                .build();
    }

    public static ConditionNode likeLeft(String field, String value) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().likeLeft(value).build())
                .build();
    }

    public static ConditionNode likeRight(String field, String value) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().likeRight(value).build())
                .build();
    }

    @SuppressWarnings("unchecked")
    public static ConditionNode in(String field, List<?> values) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().in((List) values).build())
                .build();
    }

    @SuppressWarnings("unchecked")
    public static ConditionNode notIn(String field, List<?> values) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().notIn((List) values).build())
                .build();
    }

    public static ConditionNode between(String field, Object start, Object end) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().betweenStart(start).betweenEnd(end).build())
                .build();
    }

    public static ConditionNode isNull(String field) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().isNull(true).build())
                .build();
    }

    public static ConditionNode isNotNull(String field) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(FieldCondition.builder().isNotNull(true).build())
                .build();
    }

    public static ConditionNode of(String field, FieldCondition<?> condition) {
        return ConditionNode.builder()
                .field(field)
                .fieldCondition(condition)
                .build();
    }

    public static ConditionNode and(ConditionNode... children) {
        return ConditionNode.builder()
                .logicType(LogicType.AND)
                .children(Arrays.asList(children))
                .build();
    }

    public static ConditionNode or(ConditionNode... children) {
        return ConditionNode.builder()
                .logicType(LogicType.OR)
                .children(Arrays.asList(children))
                .build();
    }

    public static ConditionNode not(ConditionNode child) {
        return ConditionNode.builder()
                .logicType(LogicType.NOT)
                .children(List.of(child))
                .build();
    }
}
