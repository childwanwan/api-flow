package com.apiflow.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    PARAM_IS_EMPTY("10001", "参数不能为空"),
    PARAM_VALIDATION_FAILED("10002", "参数校验失败"),
    DATA_NOT_EXIST("10003", "数据不存在"),




    PAGE_PARAM_IS_EMPTY("20001", "分页参数不能为空"),
    PAGE_SIZE_TOO_LARGE("20002", "每页大小不能超过1000"),


    GROUP_CODE_EXIST("30001", "分组编码已存在"),
    GROUP_CODE_EMPTY("30002", "分组编码不能为空"),
    GROUP_NAME_EMPTY("30003", "分组名称不能为空"),





    API_NOT_FOUND("10001", "API不存在"),
    API_DISABLED("10002", "API已禁用"),
    // PARAM_VALIDATION_FAILED("10003", "参数校验失败"),
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
    AUTH_INVALID_CREDENTIALS("AUTH001", "用户名或密码错误"),
    AUTH_NOT_LOGGED_IN("AUTH002", "未登录"),
    USER_NOT_FOUND("USER001", "用户不存在"),
    USER_USERNAME_EMPTY("USER002", "用户名不能为空"),
    USER_PASSWORD_EMPTY("USER003", "密码不能为空"),
    USER_USERNAME_EXISTS("USER004", "用户名已存在"),
    USER_CREATE_FAILED("USER005", "创建失败"),
    USER_UPDATE_FAILED("USER006", "更新失败"),
    USER_DELETE_FAILED("USER007", "删除失败"),
    UNKNOWN_ERROR("99999", "未知错误");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}