package com.apigateway.application.service;

import com.apigateway.application.task.TaskApplicationService;
import com.apigateway.application.task.command.TaskCancelCommand;
import com.apigateway.application.task.command.TaskRetryCommand;
import com.apigateway.application.task.command.TaskSubmitCommand;
import com.apigateway.common.exception.BusinessException;
import com.apigateway.common.exception.ErrorCode;
import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.domain.config.repository.ApiConfigRepository;
import com.apigateway.domain.config.enums.ApiConfigStatus;
import com.apigateway.domain.task.enums.ActionType;
import com.apigateway.domain.task.enums.TaskStatus;
import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.domain.task.repository.TaskRepository;
import com.apigateway.domain.task.service.TaskDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskApplicationServiceTest {

    @Mock
    private TaskDomainService taskDomainService;

    @Mock
    private ApiConfigRepository apiConfigRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskApplicationService taskApplicationService;

    @Test
    void shouldSubmitTaskSuccessfully() {
        TaskSubmitCommand command = createTaskSubmitCommand();
        ApiConfigEntity config = createApiConfigEntity();
        TaskEntity task = createTaskEntity();

        when(apiConfigRepository.findByApiCode(command.getApiCode())).thenReturn(config);
        when(taskDomainService.createTask(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(task);

        TaskEntity submitted = taskApplicationService.submitTask(command);

        assertThat(submitted).isNotNull();
        verify(taskDomainService).createTask(any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void shouldThrowExceptionWhenApiNotFound() {
        TaskSubmitCommand command = createTaskSubmitCommand();
        when(apiConfigRepository.findByApiCode(command.getApiCode())).thenReturn(null);

        assertThatThrownBy(() -> taskApplicationService.submitTask(command))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.API_NOT_FOUND.getMessage());
    }

    @Test
    void shouldGetTaskSuccessfully() {
        TaskEntity task = createTaskEntity();
        when(taskRepository.findByTaskNo(task.getTaskNo())).thenReturn(task);

        TaskEntity found = taskApplicationService.getTask(task.getTaskNo());

        assertThat(found).isNotNull();
        assertThat(found.getTaskNo()).isEqualTo(task.getTaskNo());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findByTaskNo("NON_EXISTENT")).thenReturn(null);

        assertThatThrownBy(() -> taskApplicationService.getTask("NON_EXISTENT"))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.TASK_NOT_FOUND.getMessage());
    }

    @Test
    void shouldCancelTaskSuccessfully() {
        TaskCancelCommand command = TaskCancelCommand.builder()
                .taskNo("TASK20260503001")
                .reason("Test Reason")
                .canceledBy("admin")
                .build();
        TaskEntity task = createTaskEntity();
        task.setStatus(TaskStatus.CANCELED);

        when(taskDomainService.cancelTask(any(), any(), any())).thenReturn(task);

        TaskEntity canceled = taskApplicationService.cancelTask(command);

        assertThat(canceled).isNotNull();
        verify(taskDomainService).cancelTask(any(), any(), any());
    }

    @Test
    void shouldRetryTaskSuccessfully() {
        TaskRetryCommand command = TaskRetryCommand.builder()
                .taskNo("TASK20260503001")
                .retryOperator("admin")
                .build();
        TaskEntity task = createTaskEntity();
        task.setStatus(TaskStatus.PENDING);

        when(taskDomainService.retryTask(any())).thenReturn(task);

        TaskEntity retried = taskApplicationService.retryTask(command);

        assertThat(retried).isNotNull();
        verify(taskDomainService).retryTask(any());
    }

    private TaskSubmitCommand createTaskSubmitCommand() {
        return TaskSubmitCommand.builder()
                .apiCode("TEST_API_001")
                .source("API")
                .actionType("ASYNC")
                .priority(10)
                .build();
    }

    private ApiConfigEntity createApiConfigEntity() {
        return ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .status(ApiConfigStatus.ENABLED)
                .autoRetryCount(3)
                .retryIntervalMs(5000L)
                .deleted(false)
                .build();
    }

    private TaskEntity createTaskEntity() {
        return TaskEntity.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .actionType(ActionType.ASYNC)
                .status(TaskStatus.PENDING)
                .priority(10)
                .retryCount(0)
                .maxRetryCount(3)
                .deleted(false)
                .version(0)
                .build();
    }

}
