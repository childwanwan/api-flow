package com.flow.interfaces.web;

import com.flow.application.task.TaskApplicationService;
import com.flow.application.task.command.TaskCancelCommand;
import com.flow.application.task.command.TaskRetryCommand;
import com.flow.application.task.command.TaskSubmitCommand;
import com.flow.common.result.Result;
import com.flow.domain.task.model.TaskDO;
import com.flow.interfaces.biz.task.TaskController;
import com.flow.interfaces.biz.task.dto.TaskCancelRequest;
import com.flow.interfaces.biz.task.dto.TaskRetryRequest;
import com.flow.interfaces.biz.task.dto.TaskSubmitRequest;
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
                .actionType("SYNC")
                .priority(1)
                .params(Map.of("key", "value"))
                .build();

        TaskDO task = TaskDO.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .apiCode("TEST_API")
                .status("PENDING")
                .build();

        when(taskApplicationService.submitTask(any(TaskSubmitCommand.class))).thenReturn(task);

        Result<TaskDO> result = taskController.submitTask(request);

        assertTrue(result.isSuccess());
        assertEquals("TASK20260503001", result.getData().getTaskNo());
        verify(taskApplicationService).submitTask(any(TaskSubmitCommand.class));
    }

    @Test
    void shouldGetTask() {
        TaskDO task = TaskDO.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .apiCode("TEST_API")
                .status("PENDING")
                .build();

        when(taskApplicationService.getTask("TASK20260503001")).thenReturn(task);

        Result<TaskDO> result = taskController.getTask("TASK20260503001");

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

        TaskDO task = TaskDO.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .status("CANCELED")
                .build();

        when(taskApplicationService.cancelTask(any(TaskCancelCommand.class))).thenReturn(task);

        Result<TaskDO> result = taskController.cancelTask(request);

        assertTrue(result.isSuccess());
        assertEquals("CANCELED", result.getData().getStatus());
        verify(taskApplicationService).cancelTask(any(TaskCancelCommand.class));
    }

    @Test
    void shouldRetryTask() {
        TaskRetryRequest request = TaskRetryRequest.builder()
                .taskNo("TASK20260503001")
                .retryOperator("admin")
                .build();

        TaskDO task = TaskDO.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .status("PENDING")
                .build();

        when(taskApplicationService.retryTask(any(TaskRetryCommand.class))).thenReturn(task);

        Result<TaskDO> result = taskController.retryTask(request);

        assertTrue(result.isSuccess());
        assertEquals("PENDING", result.getData().getStatus());
        verify(taskApplicationService).retryTask(any(TaskRetryCommand.class));
    }

}