package com.apiflow.common.result;

import com.apiflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private boolean success;
    private T data;
    private ErrorInfo error;

    public static <T> Result<T> success() {
        return Result.<T>builder()
                .success(true)
                .build();
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return fail(errorCode, null);
    }

    public static <T> Result<T> fail(ErrorCode errorCode, String detail) {
        return Result.<T>builder()
                .success(false)
                .error(ErrorInfo.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .detail(detail)
                        .build())
                .build();
    }

    public static <T> Result<T> fail(String code, String message, String detail) {
        return Result.<T>builder()
                .success(false)
                .error(ErrorInfo.builder()
                        .code(code)
                        .message(message)
                        .detail(detail)
                        .build())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorInfo {
        private String code;
        private String message;
        private String detail;
    }

}
