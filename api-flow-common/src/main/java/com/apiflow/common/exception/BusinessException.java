package com.apiflow.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detail;

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public BusinessException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public BusinessException(String message, ErrorCode errorCode, String detail) {
        super(message);
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public BusinessException(String message, Throwable cause, ErrorCode errorCode, String detail) {
        super(message, cause);
        this.errorCode = errorCode;
        this.detail = detail;
    }

}
