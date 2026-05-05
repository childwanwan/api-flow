package com.apiflow.domain.filter;

import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.domain.config.model.FilterRule;
import com.apiflow.domain.config.model.FilterRules;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class FilterRuleEngine {

    public static void evaluate(FilterRules filterRules, Map<String, Object> params) {
        if (filterRules == null || filterRules.getRules() == null || filterRules.getRules().isEmpty()) {
            return;
        }
        for (FilterRule rule : filterRules.getRules()) {
            Object fieldValue = getFieldValue(params, rule.getField());
            if (!evaluateRule(rule, fieldValue)) {
                String message = rule.getMessage() != null ? rule.getMessage()
                        : "参数校验失败: " + rule.getField() + " " + rule.getOperator() + " " + rule.getValue();
                log.warn("Filter rule violated: field={}, operator={}, value={}, fieldValue={}",
                        rule.getField(), rule.getOperator(), rule.getValue(), fieldValue);
                throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, message);
            }
        }
    }

    private static boolean evaluateRule(FilterRule rule, Object fieldValue) {
        String operator = rule.getOperator();
        String expectedValue = rule.getValue();

        if (expectedValue == null && !"NOT_EMPTY".equals(operator) && !"EMPTY".equals(operator)) {
            return true;
        }

        return switch (operator) {
            case "EQ" -> equals(fieldValue, expectedValue);
            case "NE" -> !equals(fieldValue, expectedValue);
            case "GT" -> compareNumbers(fieldValue, expectedValue) > 0;
            case "GTE" -> compareNumbers(fieldValue, expectedValue) >= 0;
            case "LT" -> compareNumbers(fieldValue, expectedValue) < 0;
            case "LTE" -> compareNumbers(fieldValue, expectedValue) <= 0;
            case "IN" -> isIn(fieldValue, expectedValue);
            case "NOT_IN" -> !isIn(fieldValue, expectedValue);
            case "MATCHES" -> matches(fieldValue, expectedValue);
            case "NOT_MATCHES" -> !matches(fieldValue, expectedValue);
            case "NOT_EMPTY" -> fieldValue != null && !fieldValue.toString().isEmpty();
            case "EMPTY" -> fieldValue == null || fieldValue.toString().isEmpty();
            default -> true;
        };
    }

    private static Object getFieldValue(Map<String, Object> params, String fieldPath) {
        if (params == null || fieldPath == null) return null;
        String[] parts = fieldPath.split("\\.");
        Object current = params;
        for (String part : parts) {
            if (current instanceof Map<?, ?> map) {
                current = map.get(part);
            } else {
                return null;
            }
        }
        return current;
    }

    private static boolean equals(Object actual, String expected) {
        if (actual == null) return false;
        return actual.toString().equals(expected);
    }

    private static int compareNumbers(Object actual, String expected) {
        try {
            double actualNum = Double.parseDouble(actual.toString());
            double expectedNum = Double.parseDouble(expected);
            return Double.compare(actualNum, expectedNum);
        } catch (NumberFormatException e) {
            return actual.toString().compareTo(expected);
        }
    }

    private static boolean isIn(Object actual, String expected) {
        if (actual == null) return false;
        String[] values = expected.split(",");
        String actualStr = actual.toString();
        for (String v : values) {
            if (actualStr.equals(v.trim())) return true;
        }
        return false;
    }

    private static boolean matches(Object actual, String regex) {
        if (actual == null) return false;
        return actual.toString().matches(regex);
    }
}
