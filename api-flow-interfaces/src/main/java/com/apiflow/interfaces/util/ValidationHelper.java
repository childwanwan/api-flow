package com.apiflow.interfaces.util;

import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;

import java.util.Map;

public final class ValidationHelper {

    private ValidationHelper() {
    }

    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " cannot be blank");
        }
    }

    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " cannot be null");
        }
    }

    public static void validateSize(String value, int max, String fieldName) {
        if (value != null && value.length() > max) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " length must be less than or equal to " + max);
        }
    }

    public static void validatePattern(String value, String regex, String fieldName) {
        validatePattern(value, regex, fieldName, fieldName + " format is invalid");
    }

    public static void validatePattern(String value, String regex, String fieldName, String errorMessage) {
        if (value != null && !value.matches(regex)) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, errorMessage);
        }
    }

    public static void validateRange(Long value, Long min, Long max, String fieldName) {
        if (value != null && (value < min || value > max)) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " must be between " + min + " and " + max);
        }
    }

    public static void validateRange(Integer value, Integer min, Integer max, String fieldName) {
        if (value != null && (value < min || value > max)) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " must be between " + min + " and " + max);
        }
    }

    public static void validateNonNegative(Integer value, String fieldName) {
        if (value != null && value < 0) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " must be non-negative");
        }
    }

    public static void validateMax(Integer value, int max, String fieldName) {
        if (value != null && value > max) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " must be at most " + max);
        }
    }

    public static void validateMapSize(Map<?, ?> map, int max, String fieldName) {
        if (map != null && map.size() > max) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, fieldName + " size must be less than or equal to " + max);
        }
    }
}
