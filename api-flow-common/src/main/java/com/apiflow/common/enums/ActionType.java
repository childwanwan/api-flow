package com.apiflow.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionType {

    SYNC("SYNC"),
    ASYNC("ASYNC");

    private final String value;

    public static ActionType of(String value) {
        for (ActionType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
