package com.flow.infrastructure.persistence.repository;

import com.flow.api.repository.task.param.SaveTaskParam;
import com.flow.api.repository.task.param.SelectOneTaskParam;
import com.flow.infrastructure.persistence.mybatis.task.TaskMapper;
import com.flow.infrastructure.persistence.mybatis.task.TaskRepositoryImpl;
import com.flow.infrastructure.persistence.mybatis.task.entity.TaskPO;
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

    private TaskRepositoryImpl taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository = new TaskRepositoryImpl(taskMapper);
    }

    @Test
    void shouldSaveTask() {
        SaveTaskParam param = createSaveTaskParam();

        when(taskMapper.insert(any(TaskPO.class))).thenReturn(1);

        taskRepository.save(param);

        verify(taskMapper).insert(any(TaskPO.class));
    }

    @Test
    void shouldQueryTaskByTaskNo() {
        TaskPO taskPO = createTaskPO();

        when(taskMapper.selectOne(any())).thenReturn(taskPO);

        taskRepository.selectOne(SelectOneTaskParam.builder().taskNo("TASK20260503001").build());

        verify(taskMapper).selectOne(any());
    }

    @Test
    void shouldReturnNullWhenTaskNotFound() {
        when(taskMapper.selectOne(any())).thenReturn(null);

        var found = taskRepository.selectOne(SelectOneTaskParam.builder().taskNo("NOT_EXISTS").build());

        assertThat(found).isNull();
    }

    private SaveTaskParam createSaveTaskParam() {
        return SaveTaskParam.builder()
                .taskNo("TASK20260503001")
                .source("API")
                .apiCode("TEST_API")
                .apiName("Test API")
                .actionType("SYNC")
                .status("PENDING")
                .interruptFlag(false)
                .compensateStatus("NONE")
                .priority(10)
                .retryCount(0)
                .maxRetryCount(3)
                .build();
    }

    private TaskPO createTaskPO() {
        return TaskPO.builder()
                .id(1L)
                .taskNo("TASK20260503001")
                .source("API")
                .apiCode("TEST_API")
                .apiName("Test API")
                .actionType("SYNC")
                .status("PENDING")
                .interruptFlag(false)
                .compensateStatus("NONE")
                .priority(10)
                .retryCount(0)
                .maxRetryCount(3)
                .version(0)
                .build();
    }

}