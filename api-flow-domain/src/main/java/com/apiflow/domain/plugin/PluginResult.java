package com.apiflow.domain.plugin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginResult {

    private boolean success;
    private String message;
    private Object data;
    private String errorCode;

    public static PluginResult success() {
        return PluginResult.builder().success(true).build();
    }

    public static PluginResult success(Object data) {
        return PluginResult.builder().success(true).data(data).build();
    }

    public static PluginResult success(String message, Object data) {
        return PluginResult.builder().success(true).message(message).data(data).build();
    }

    public static PluginResult fail(String message) {
        return PluginResult.builder().success(false).message(message).build();
    }

    public static PluginResult fail(String errorCode, String message) {
        return PluginResult.builder().success(false).errorCode(errorCode).message(message).build();
    }
}
