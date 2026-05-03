package com.apigateway.domain.task.enums;

import lombok.Getter;

@Getter
public enum CompensateStatus {

    NONE("NONE", "无需补偿"),
    PENDING("PENDING", "待补偿"),
    RUNNING("RUNNING", "补偿中"),
    SUCCESS("SUCCESS", "补偿成功"),
    FAILED("FAILED", "补偿失败");

    private final String code;
    private final String desc;

    CompensateStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CompensateStatus fromCode(String code) {
        for (CompensateStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    public boolean canCompensate() {
        return this == PENDING || this == FAILED;
    }

}
