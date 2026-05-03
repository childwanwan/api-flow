package com.apigateway.domain.task.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskStatusTest {

    @Test
    void shouldReturnCorrectCodeAndDescription() {
        assertThat(TaskStatus.PENDING.getCode()).isEqualTo("PENDING");
        assertThat(TaskStatus.PENDING.getDesc()).isEqualTo("待处理");

        assertThat(TaskStatus.RUNNING.getCode()).isEqualTo("RUNNING");
        assertThat(TaskStatus.RUNNING.getDesc()).isEqualTo("执行中");

        assertThat(TaskStatus.SUCCESS.getCode()).isEqualTo("SUCCESS");
        assertThat(TaskStatus.SUCCESS.getDesc()).isEqualTo("成功");

        assertThat(TaskStatus.FAILED.getCode()).isEqualTo("FAILED");
        assertThat(TaskStatus.FAILED.getDesc()).isEqualTo("失败");

        assertThat(TaskStatus.CANCELED.getCode()).isEqualTo("CANCELED");
        assertThat(TaskStatus.CANCELED.getDesc()).isEqualTo("已取消");
    }

    @Test
    void shouldReturnStatusFromCode() {
        assertThat(TaskStatus.fromCode("PENDING")).isEqualTo(TaskStatus.PENDING);
        assertThat(TaskStatus.fromCode("RUNNING")).isEqualTo(TaskStatus.RUNNING);
        assertThat(TaskStatus.fromCode("SUCCESS")).isEqualTo(TaskStatus.SUCCESS);
        assertThat(TaskStatus.fromCode("FAILED")).isEqualTo(TaskStatus.FAILED);
        assertThat(TaskStatus.fromCode("CANCELED")).isEqualTo(TaskStatus.CANCELED);
        assertThat(TaskStatus.fromCode("INVALID")).isNull();
    }

    @Test
    void shouldCheckIfCanExecute() {
        assertThat(TaskStatus.PENDING.canExecute()).isTrue();
        assertThat(TaskStatus.RUNNING.canExecute()).isFalse();
        assertThat(TaskStatus.SUCCESS.canExecute()).isFalse();
        assertThat(TaskStatus.FAILED.canExecute()).isFalse();
        assertThat(TaskStatus.CANCELED.canExecute()).isFalse();
    }

    @Test
    void shouldCheckIfCanCancel() {
        assertThat(TaskStatus.PENDING.canCancel()).isTrue();
        assertThat(TaskStatus.RUNNING.canCancel()).isTrue();
        assertThat(TaskStatus.SUCCESS.canCancel()).isFalse();
        assertThat(TaskStatus.FAILED.canCancel()).isFalse();
        assertThat(TaskStatus.CANCELED.canCancel()).isFalse();
    }

    @Test
    void shouldCheckIfCanRetry() {
        assertThat(TaskStatus.PENDING.canRetry()).isFalse();
        assertThat(TaskStatus.RUNNING.canRetry()).isFalse();
        assertThat(TaskStatus.SUCCESS.canRetry()).isFalse();
        assertThat(TaskStatus.FAILED.canRetry()).isTrue();
        assertThat(TaskStatus.CANCELED.canRetry()).isFalse();
    }

}
