package com.apigateway.infrastructure.persistence.repository;

import com.apigateway.infrastructure.persistence.mybatis.task.TaskRepositoryImpl;
import com.apigateway.domain.task.enums.ActionType;
import com.apigateway.domain.task.enums.CompensateStatus;
import com.apigateway.domain.task.enums.TaskStatus;
import com.apigateway.domain.task.model.ExecInfo;
import com.apigateway.domain.task.model.RequestContext;
import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.domain.task.query.TaskQuery;
import com.apigateway.infrastructure.config.TestInfrastructureConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestInfrastructureConfig.class)
@ActiveProfiles("test")
class TaskRepositoryImplTest {

    @Autowired
    private TaskRepositoryImpl taskRepository;

    @Test
    void shouldSaveAndFindByTaskNo() {
        TaskEntity task = createTask();
        TaskEntity saved = taskRepository.save(task);
        assertThat(saved.getId()).isNotNull();

        TaskEntity found = taskRepository.query(TaskQuery.builder().taskNo(task.getTaskNo()).build());
        assertThat(found).isNotNull();
        assertThat(found.getTaskNo()).isEqualTo(task.getTaskNo());
    }

    @Test
    void shouldUpdateTask() {
        TaskEntity task = createTask();
        TaskEntity saved = taskRepository.save(task);

        saved.setStatus(TaskStatus.SUCCESS);
        TaskEntity updated = taskRepository.update(saved);

        assertThat(updated.getStatus()).isEqualTo(TaskStatus.SUCCESS);
        assertThat(updated.getVersion()).isEqualTo(saved.getVersion() + 1);
    }

    @Test
    void shouldCheckIfExistsByTaskNo() {
        TaskEntity task = createTask();
        taskRepository.save(task);

        boolean exists = taskRepository.exists(TaskQuery.builder().taskNo(task.getTaskNo()).build());
        assertThat(exists).isTrue();

        boolean notExists = taskRepository.exists(TaskQuery.builder().taskNo("NOT_EXISTS").build());
        assertThat(notExists).isFalse();
    }

    private TaskEntity createTask() {
        return TaskEntity.builder()
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

}