package com.apigateway.domain.task.model;

import com.apigateway.domain.task.enums.ActionType;
import com.apigateway.domain.task.enums.CompensateStatus;
import com.apigateway.domain.task.enums.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskEntityTest {

    @Test
    void shouldStartExecute() {
        TaskEntity task = createTask();
        task.startExecute();
        assertThat(task.getStatus()).isEqualTo(TaskStatus.RUNNING);
        assertThat(task.getStartExecuteTimeMs()).isNotNull();
        assertThat(task.getUpdateTimeMs()).isNotNull();
    }

    @Test
    void shouldCompleteSuccessfully() {
        TaskEntity task = createTask();
        task.startExecute();
        Object responseData = "Success";
        task.complete(responseData);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.SUCCESS);
        assertThat(task.getResponseData()).isEqualTo(responseData);
        assertThat(task.getEndExecuteTimeMs()).isNotNull();
        assertThat(task.getExecuteDurationMs()).isNotNull();
        assertThat(task.getCompensateStatus()).isEqualTo(CompensateStatus.SUCCESS);
        assertThat(task.getUpdateTimeMs()).isNotNull();
    }

    @Test
    void shouldFailWithErrorMessage() {
        TaskEntity task = createTask();
        task.startExecute();
        String errorMessage = "Test error";
        task.fail(errorMessage);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.FAILED);
        assertThat(task.getEndExecuteTimeMs()).isNotNull();
        assertThat(task.getCompensateStatus()).isEqualTo(CompensateStatus.PENDING);
        assertThat(task.getUpdateTimeMs()).isNotNull();
    }

    @Test
    void shouldCancelWithReason() {
        TaskEntity task = createTask();
        String reason = "User canceled";
        String canceledBy = "admin";
        task.cancel(reason, canceledBy);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.CANCELED);
        assertThat(task.getInterruptFlag()).isTrue();
        assertThat(task.getCancelReason()).isEqualTo(reason);
        assertThat(task.getCanceledBy()).isEqualTo(canceledBy);
        assertThat(task.getCancelTimeMs()).isNotNull();
        assertThat(task.getUpdateTimeMs()).isNotNull();
    }

    @Test
    void shouldPrepareRetry() {
        TaskEntity task = createTask();
        task.fail("Test error");
        task.prepareRetry();
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(task.getInterruptFlag()).isFalse();
        assertThat(task.getRetryCount()).isEqualTo(1);
        assertThat(task.getNextRetryTimeMs()).isNotNull();
        assertThat(task.getUpdateTimeMs()).isNotNull();
    }

    @Test
    void shouldCheckIfCanExecute() {
        TaskEntity task = createTask();
        assertThat(task.canExecute()).isTrue();
        task.startExecute();
        assertThat(task.canExecute()).isFalse();
    }

    @Test
    void shouldCheckIfCanCancel() {
        TaskEntity task = createTask();
        assertThat(task.canCancel()).isTrue();
        task.startExecute();
        assertThat(task.canCancel()).isTrue();
        task.complete("Success");
        assertThat(task.canCancel()).isFalse();
    }

    @Test
    void shouldCheckIfCanRetry() {
        TaskEntity task = createTask();
        assertThat(task.canRetry()).isFalse();
        task.fail("Test error");
        assertThat(task.canRetry()).isTrue();
        task.prepareRetry();
        assertThat(task.canRetry()).isFalse();
    }

    private TaskEntity createTask() {
        return TaskEntity.builder()
                .taskNo("TASK20260503001")
                .apiCode("TEST_API")
                .apiName("Test API")
                .source("API")
                .actionType(ActionType.SYNC)
                .status(TaskStatus.PENDING)
                .interruptFlag(false)
                .compensateStatus(CompensateStatus.NONE)
                .priority(10)
                .requestContext(RequestContext.builder().build())
                .execInfo(ExecInfo.builder().build())
                .retryCount(0)
                .maxRetryCount(3)
                .deleted(false)
                .version(0)
                .build();
    }

}
