package com.apiflow.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnableStatus {

    ENABLED("ENABLED"),
    DISABLED("DISABLED");

    private final String value;

    public static EnableStatus of(String value) {
        for (EnableStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
