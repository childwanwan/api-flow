package com.apigateway.domain.task.enums;

import lombok.Getter;

@Getter
public enum ActionType {

    SYNC("SYNC", "同步执行"),
    ASYNC("ASYNC", "异步执行");

    private final String code;
    private final String desc;

    ActionType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ActionType fromCode(String code) {
        for (ActionType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return ASYNC;
    }

}
