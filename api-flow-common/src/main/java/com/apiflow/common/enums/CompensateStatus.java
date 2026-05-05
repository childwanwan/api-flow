package com.apiflow.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompensateStatus {

    NONE("NONE"),
    PENDING("PENDING"),
    RUNNING("RUNNING"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED");

    private final String value;

    public static CompensateStatus of(String value) {
        for (CompensateStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
