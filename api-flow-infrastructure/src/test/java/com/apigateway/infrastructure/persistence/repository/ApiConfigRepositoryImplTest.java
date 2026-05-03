package com.apigateway.infrastructure.persistence.repository;

import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.domain.config.enums.ApiConfigStatus;
import com.apigateway.domain.config.query.ApiConfigQuery;
import com.apigateway.infrastructure.config.TestInfrastructureConfig;
import com.apigateway.infrastructure.persistence.mybatis.config.ApiConfigRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestInfrastructureConfig.class)
@ActiveProfiles("test")
class ApiConfigRepositoryImplTest {

    @Autowired
    private ApiConfigRepositoryImpl apiConfigRepository;

    @Test
    void shouldSaveAndFindByApiCode() {
        ApiConfigEntity config = createApiConfig();
        ApiConfigEntity saved = apiConfigRepository.save(config);
        assertThat(saved.getId()).isNotNull();

        ApiConfigEntity found = apiConfigRepository.query(ApiConfigQuery.builder().apiCode(config.getApiCode()).build());
        assertThat(found).isNotNull();
        assertThat(found.getApiCode()).isEqualTo(config.getApiCode());
    }

    @Test
    void shouldUpdateConfig() {
        ApiConfigEntity config = createApiConfig();
        ApiConfigEntity saved = apiConfigRepository.save(config);

        saved.setApiName("Updated Name");
        saved.setStatus(ApiConfigStatus.DISABLED);
        ApiConfigEntity updated = apiConfigRepository.update(saved);

        assertThat(updated.getApiName()).isEqualTo("Updated Name");
        assertThat(updated.getStatus()).isEqualTo(ApiConfigStatus.DISABLED);
        assertThat(updated.getVersion()).isEqualTo(saved.getVersion() + 1);
    }

    @Test
    void shouldCheckIfExistsByApiCode() {
        ApiConfigEntity config = createApiConfig();
        apiConfigRepository.save(config);

        boolean exists = apiConfigRepository.exists(ApiConfigQuery.builder().apiCode(config.getApiCode()).build());
        assertThat(exists).isTrue();

        boolean notExists = apiConfigRepository.exists(ApiConfigQuery.builder().apiCode("NOT_EXISTS").build());
        assertThat(notExists).isFalse();
    }

    private ApiConfigEntity createApiConfig() {
        return ApiConfigEntity.builder()
                .groupNo("GROUP1")
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .apiDescription("Test Description")
                .status(ApiConfigStatus.ENABLED)
                .requestTimeoutMs(30000L)
                .autoRetryCount(3)
                .retryIntervalMs(5000L)
                .maxQueueSize(10000)
                .build();
    }

}