package com.apigateway.interfaces.web;

import com.apigateway.application.task.TaskApplicationService;
import com.apigateway.application.task.command.TaskCancelCommand;
import com.apigateway.application.task.command.TaskRetryCommand;
import com.apigateway.application.task.command.TaskSubmitCommand;
import com.apigateway.common.result.Result;
import com.apigateway.domain.task.enums.ActionType;
import com.apigateway.domain.task.enums.TaskStatus;
import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.interfaces.task.TaskController;
import com.apigateway.interfaces.task.dto.TaskCancelRequest;
import com.apigateway.interfaces.task.dto.TaskRetryRequest;
import com.apigateway.interfaces.task.dto.TaskSubmitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @Mock
    private TaskApplicationService taskApplicationService;

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskController = new TaskController(taskApplicationService);
    }

    @Test
    void shouldSubmitTask() {
        TaskSubmitRequest request = TaskSubmitRequest.builder()
                .apiCode("TEST_API")
                .source("TEST_SOURCE")
                .groupNo("GROUP_001")
                .actionType(ActionType.SYNC.getCode())
                .priority(1)
                .params(Map.of("key", "value"))
                .build();

        TaskEntity entity = TaskEntity.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .apiCode("TEST_API")
                .status(TaskStatus.PENDING)
                .build();

        when(taskApplicationService.submitTask(any(TaskSubmitCommand.class))).thenReturn(entity);

        Result<TaskEntity> result = taskController.submitTask(request);

        assertTrue(result.isSuccess());
        assertEquals("TASK20260503001", result.getData().getTaskNo());
        verify(taskApplicationService).submitTask(any(TaskSubmitCommand.class));
    }

    @Test
    void shouldGetTask() {
        TaskEntity entity = TaskEntity.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .apiCode("TEST_API")
                .status(TaskStatus.PENDING)
                .build();

        when(taskApplicationService.getTask("TASK20260503001")).thenReturn(entity);

        Result<TaskEntity> result = taskController.getTask("TASK20260503001");

        assertTrue(result.isSuccess());
        assertEquals("TASK20260503001", result.getData().getTaskNo());
        verify(taskApplicationService).getTask("TASK20260503001");
    }

    @Test
    void shouldCancelTask() {
        TaskCancelRequest request = TaskCancelRequest.builder()
                .taskNo("TASK20260503001")
                .cancelReason("User canceled")
                .canceledBy("admin")
                .build();

        TaskEntity entity = TaskEntity.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .status(TaskStatus.CANCELED)
                .build();

        when(taskApplicationService.cancelTask(any(TaskCancelCommand.class))).thenReturn(entity);

        Result<TaskEntity> result = taskController.cancelTask(request);

        assertTrue(result.isSuccess());
        assertEquals(TaskStatus.CANCELED, result.getData().getStatus());
        verify(taskApplicationService).cancelTask(any(TaskCancelCommand.class));
    }

    @Test
    void shouldRetryTask() {
        TaskRetryRequest request = TaskRetryRequest.builder()
                .taskNo("TASK20260503001")
                .retryOperator("admin")
                .build();

        TaskEntity entity = TaskEntity.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .status(TaskStatus.PENDING)
                .build();

        when(taskApplicationService.retryTask(any(TaskRetryCommand.class))).thenReturn(entity);

        Result<TaskEntity> result = taskController.retryTask(request);

        assertTrue(result.isSuccess());
        assertEquals(TaskStatus.PENDING, result.getData().getStatus());
        verify(taskApplicationService).retryTask(any(TaskRetryCommand.class));
    }

}