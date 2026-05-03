package com.apigateway.domain.config.enums;

import lombok.Getter;

@Getter
public enum ApiConfigStatus {

    ENABLED("ENABLED", "启用"),
    DISABLED("DISABLED", "禁用");

    private final String code;
    private final String desc;

    ApiConfigStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ApiConfigStatus fromCode(String code) {
        for (ApiConfigStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    public boolean isEnabled() {
        return this == ENABLED;
    }

}
