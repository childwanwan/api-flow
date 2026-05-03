package com.apigateway.domain.task.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {

    PENDING("PENDING", "待执行"),
    RUNNING("RUNNING", "执行中"),
    SUCCESS("SUCCESS", "执行成功"),
    FAILED("FAILED", "执行失败"),
    CANCELED("CANCELED", "已取消");

    private final String code;
    private final String desc;

    TaskStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TaskStatus fromCode(String code) {
        for (TaskStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    public boolean canExecute() {
        return this == PENDING;
    }

    public boolean canCancel() {
        return this == PENDING || this == RUNNING;
    }

    public boolean canRetry() {
        return this == FAILED;
    }

}
