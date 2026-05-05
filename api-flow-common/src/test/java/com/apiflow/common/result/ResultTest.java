package com.apiflow.common.result;

import com.apiflow.common.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultTest {

    @Test
    void shouldReturnSuccessResultWithoutData() {
        Result<Void> result = Result.success();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isNull();
        assertThat(result.getError()).isNull();
    }

    @Test
    void shouldReturnSuccessResultWithData() {
        String data = "test data";
        Result<String> result = Result.success(data);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isEqualTo(data);
        assertThat(result.getError()).isNull();
    }

    @Test
    void shouldReturnFailResultWithErrorCode() {
        Result<Void> result = Result.fail(ErrorCode.API_NOT_FOUND);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getError()).isNotNull();
        assertThat(result.getError().getCode()).isEqualTo(ErrorCode.API_NOT_FOUND.getCode());
        assertThat(result.getError().getMessage()).isEqualTo(ErrorCode.API_NOT_FOUND.getMessage());
        assertThat(result.getError().getDetail()).isNull();
    }

    @Test
    void shouldReturnFailResultWithErrorCodeAndDetail() {
        String detail = "apiCode=test";
        Result<Void> result = Result.fail(ErrorCode.API_NOT_FOUND, detail);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getError()).isNotNull();
        assertThat(result.getError().getCode()).isEqualTo(ErrorCode.API_NOT_FOUND.getCode());
        assertThat(result.getError().getMessage()).isEqualTo(ErrorCode.API_NOT_FOUND.getMessage());
        assertThat(result.getError().getDetail()).isEqualTo(detail);
    }

    @Test
    void shouldReturnFailResultWithCodeMessageAndDetail() {
        String code = "99999";
        String message = "Test error";
        String detail = "Test detail";
        Result<Void> result = Result.fail(code, message, detail);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getError()).isNotNull();
        assertThat(result.getError().getCode()).isEqualTo(code);
        assertThat(result.getError().getMessage()).isEqualTo(message);
        assertThat(result.getError().getDetail()).isEqualTo(detail);
    }

}
