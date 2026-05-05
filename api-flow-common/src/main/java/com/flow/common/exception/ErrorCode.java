package com.flow.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    API_NOT_FOUND("10001", "API不存在"),
    API_DISABLED("10002", "API已禁用"),
    PARAM_VALIDATION_FAILED("10003", "参数校验失败"),
    RATE_LIMIT_TRIGGERED("10004", "限流触发"),
    SYSTEM_BUSY("10005", "系统繁忙"),
    TASK_NOT_FOUND("10006", "任务不存在"),
    TASK_STATUS_NOT_ALLOWED("10007", "任务状态不允许此操作"),
    PLUGIN_NOT_FOUND("10008", "插件不存在"),
    EXECUTE_TIMEOUT("10009", "执行超时"),
    EXECUTE_FAILED("10010", "执行失败"),
    PLUGIN_EXECUTE_FAILED("10011", "插件执行失败"),
    COMPENSATE_TIMEOUT("10012", "补偿超时"),
    COMPENSATE_FAILED("10013", "补偿失败"),
    PLUGIN_IN_USE("10014", "插件正在使用中，无法卸载"),
    UNKNOWN_ERROR("99999", "未知错误");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}