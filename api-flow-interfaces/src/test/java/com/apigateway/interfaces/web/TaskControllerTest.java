package com.apigateway.interfaces.web;

import com.apigateway.application.service.TaskApplicationService;
import com.apigateway.domain.task.enums.ActionType;
import com.apigateway.domain.task.enums.TaskStatus;
import com.apigateway.domain.task.model.ExecInfo;
import com.apigateway.domain.task.model.RequestContext;
import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.interfaces.web.dto.TaskCancelRequest;
import com.apigateway.interfaces.web.dto.TaskRetryRequest;
import com.apigateway.interfaces.web.dto.TaskSubmitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskApplicationService taskApplicationService;

    @Test
    void shouldSubmitTask() throws Exception {
        TaskSubmitRequest request = new TaskSubmitRequest();
        request.setTaskNo("TASK20260503001");
        request.setApiCode("TEST_API");
        request.setActionType(ActionType.SYNC);

        TaskEntity entity = TaskEntity.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .apiCode("TEST_API")
                .status(TaskStatus.PENDING)
                .build();

        when(taskApplicationService.submitTask(any(TaskEntity.class))).thenReturn(entity);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldGetTaskByTaskNo() throws Exception {
        TaskEntity entity = TaskEntity.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .apiCode("TEST_API")
                .status(TaskStatus.PENDING)
                .build();

        when(taskApplicationService.getTaskByTaskNo("TASK20260503001")).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/api/v1/tasks/task-no/TASK20260503001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldCancelTask() throws Exception {
        TaskCancelRequest request = new TaskCancelRequest();
        request.setCancelReason("User canceled");
        request.setCancelOperator("admin");

        TaskEntity entity = TaskEntity.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .status(TaskStatus.CANCELED)
                .build();

        when(taskApplicationService.cancelTask(any(String.class), any(String.class), any(String.class))).thenReturn(entity);

        mockMvc.perform(post("/api/v1/tasks/TASK20260503001/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldRetryTask() throws Exception {
        TaskRetryRequest request = new TaskRetryRequest();
        request.setRetryOperator("admin");

        TaskEntity entity = TaskEntity.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .status(TaskStatus.PENDING)
                .build();

        when(taskApplicationService.retryTask(any(String.class))).thenReturn(entity);

        mockMvc.perform(post("/api/v1/tasks/TASK20260503001/retry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

}
