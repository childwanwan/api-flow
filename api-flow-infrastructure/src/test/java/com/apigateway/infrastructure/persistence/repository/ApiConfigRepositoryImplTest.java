package com.apigateway.infrastructure.persistence.repository;

import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.domain.config.enums.ApiConfigStatus;
import com.apigateway.domain.config.query.ApiConfigQuery;
import com.apigateway.infrastructure.persistence.mybatis.config.ApiConfigConverter;
import com.apigateway.infrastructure.persistence.mybatis.config.ApiConfigMapper;
import com.apigateway.infrastructure.persistence.mybatis.config.ApiConfigRepositoryImpl;
import com.apigateway.infrastructure.persistence.mybatis.config.entity.ApiConfigDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiConfigRepositoryImplTest {

    @Mock
    private ApiConfigMapper apiConfigMapper;

    @Mock
    private ApiConfigConverter apiConfigConverter;

    private ApiConfigRepositoryImpl apiConfigRepository;

    @BeforeEach
    void setUp() {
        apiConfigRepository = new ApiConfigRepositoryImpl(apiConfigMapper, apiConfigConverter);
    }

    @Test
    void shouldSaveConfig() {
        ApiConfigEntity config = createApiConfig();
        ApiConfigDO configDO = createApiConfigDO(config);

        when(apiConfigConverter.toDO(any(ApiConfigEntity.class))).thenReturn(configDO);
        when(apiConfigMapper.insert(any(ApiConfigDO.class))).thenReturn(1);
        when(apiConfigConverter.toEntity(any(ApiConfigDO.class))).thenReturn(config);

        ApiConfigEntity saved = apiConfigRepository.save(config);

        assertThat(saved).isNotNull();
        verify(apiConfigMapper).insert(any(ApiConfigDO.class));
    }

    @Test
    void shouldQueryConfigByApiCode() {
        ApiConfigEntity config = createApiConfig();
        ApiConfigDO configDO = createApiConfigDO(config);

        when(apiConfigMapper.selectOne(any())).thenReturn(configDO);
        when(apiConfigConverter.toEntity(any(ApiConfigDO.class))).thenReturn(config);

        ApiConfigEntity found = apiConfigRepository.query(ApiConfigQuery.builder().apiCode(config.getApiCode()).build());

        assertThat(found).isNotNull();
        assertThat(found.getApiCode()).isEqualTo(config.getApiCode());
    }

    @Test
    void shouldReturnNullWhenConfigNotFound() {
        when(apiConfigMapper.selectOne(any())).thenReturn(null);

        ApiConfigEntity found = apiConfigRepository.query(ApiConfigQuery.builder().apiCode("NOT_EXISTS").build());

        assertThat(found).isNull();
    }

    @Test
    void shouldCheckIfExistsByApiCode() {
        ApiConfigEntity config = createApiConfig();

        when(apiConfigMapper.exists(any())).thenReturn(true);

        boolean exists = apiConfigRepository.exists(ApiConfigQuery.builder().apiCode(config.getApiCode()).build());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenConfigNotExists() {
        when(apiConfigMapper.exists(any())).thenReturn(false);

        boolean notExists = apiConfigRepository.exists(ApiConfigQuery.builder().apiCode("NOT_EXISTS").build());

        assertThat(notExists).isFalse();
    }

    private ApiConfigEntity createApiConfig() {
        return ApiConfigEntity.builder()
                .id(1L)
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

    private ApiConfigDO createApiConfigDO(ApiConfigEntity entity) {
        return ApiConfigDO.builder()
                .id(entity.getId())
                .groupNo(entity.getGroupNo())
                .apiCode(entity.getApiCode())
                .apiName(entity.getApiName())
                .apiDescription(entity.getApiDescription())
                .status(entity.getStatus().getCode())
                .requestTimeoutMs(entity.getRequestTimeoutMs())
                .autoRetryCount(entity.getAutoRetryCount())
                .retryIntervalMs(entity.getRetryIntervalMs())
                .maxQueueSize(entity.getMaxQueueSize())
                .build();
    }

}