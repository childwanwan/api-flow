package com.apigateway.infrastructure.persistence.repository;

import com.apigateway.domain.task.enums.ActionType;
import com.apigateway.domain.task.enums.CompensateStatus;
import com.apigateway.domain.task.enums.TaskStatus;
import com.apigateway.domain.task.model.ExecInfo;
import com.apigateway.domain.task.model.RequestContext;
import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.domain.task.query.TaskQuery;
import com.apigateway.infrastructure.persistence.mybatis.task.TaskConverter;
import com.apigateway.infrastructure.persistence.mybatis.task.TaskMapper;
import com.apigateway.infrastructure.persistence.mybatis.task.TaskRepositoryImpl;
import com.apigateway.infrastructure.persistence.mybatis.task.entity.TaskDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryImplTest {

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskConverter taskConverter;

    private TaskRepositoryImpl taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConverter);
    }

    @Test
    void shouldSaveTask() {
        TaskEntity task = createTask();
        TaskDO taskDO = createTaskDO(task);

        when(taskConverter.toDO(any(TaskEntity.class))).thenReturn(taskDO);
        when(taskMapper.insert(any(TaskDO.class))).thenReturn(1);
        when(taskConverter.toEntity(any(TaskDO.class))).thenReturn(task);

        TaskEntity saved = taskRepository.save(task);

        assertThat(saved).isNotNull();
        verify(taskMapper).insert(any(TaskDO.class));
    }

    @Test
    void shouldQueryTaskByTaskNo() {
        TaskEntity task = createTask();
        TaskDO taskDO = createTaskDO(task);

        when(taskMapper.selectOne(any())).thenReturn(taskDO);
        when(taskConverter.toEntity(any(TaskDO.class))).thenReturn(task);

        TaskEntity found = taskRepository.query(TaskQuery.builder().taskNo(task.getTaskNo()).build());

        assertThat(found).isNotNull();
        assertThat(found.getTaskNo()).isEqualTo(task.getTaskNo());
    }

    @Test
    void shouldReturnNullWhenTaskNotFound() {
        when(taskMapper.selectOne(any())).thenReturn(null);

        TaskEntity found = taskRepository.query(TaskQuery.builder().taskNo("NOT_EXISTS").build());

        assertThat(found).isNull();
    }

    @Test
    void shouldCheckIfExistsByTaskNo() {
        when(taskMapper.exists(any())).thenReturn(true);

        boolean exists = taskRepository.exists(TaskQuery.builder().taskNo("TASK20260503001").build());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenTaskNotExists() {
        when(taskMapper.exists(any())).thenReturn(false);

        boolean notExists = taskRepository.exists(TaskQuery.builder().taskNo("NOT_EXISTS").build());

        assertThat(notExists).isFalse();
    }

    private TaskEntity createTask() {
        return TaskEntity.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .source("API")
                .apiCode("TEST_API")
                .apiName("Test API")
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

    private TaskDO createTaskDO(TaskEntity entity) {
        return TaskDO.builder()
                .id(entity.getId())
                .taskNo(entity.getTaskNo())
                .source(entity.getSource())
                .apiCode(entity.getApiCode())
                .apiName(entity.getApiName())
                .actionType(entity.getActionType().getCode())
                .status(entity.getStatus().getCode())
                .interruptFlag(entity.getInterruptFlag())
                .compensateStatus(entity.getCompensateStatus().getCode())
                .priority(entity.getPriority())
                .retryCount(entity.getRetryCount())
                .maxRetryCount(entity.getMaxRetryCount())
                .version(entity.getVersion())
                .build();
    }

}