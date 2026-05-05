package com.flow.infrastructure.persistence.repository;

import com.flow.api.repository.config.dto.ApiConfigIDTO;
import com.flow.api.repository.config.param.SaveApiConfigParam;
import com.flow.api.repository.config.param.SelectOneApiConfigParam;
import com.flow.infrastructure.persistence.mybatis.config.ApiConfigMapper;
import com.flow.infrastructure.persistence.mybatis.config.ApiConfigRepositoryImpl;
import com.flow.infrastructure.persistence.mybatis.config.entity.ApiConfigPO;
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

    private ApiConfigRepositoryImpl apiConfigRepository;

    @BeforeEach
    void setUp() {
        apiConfigRepository = new ApiConfigRepositoryImpl(apiConfigMapper);
    }

    @Test
    void shouldSaveConfig() {
        SaveApiConfigParam param = createSaveApiConfigParam();
        ApiConfigPO configPO = createApiConfigPO();

        when(apiConfigMapper.insert(any(ApiConfigPO.class))).thenReturn(1);

        apiConfigRepository.save(param);

        verify(apiConfigMapper).insert(any(ApiConfigPO.class));
    }

    @Test
    void shouldQueryConfigByApiCode() {
        ApiConfigPO configPO = createApiConfigPO();

        when(apiConfigMapper.selectOne(any())).thenReturn(configPO);

        apiConfigRepository.selectOne(SelectOneApiConfigParam.builder().apiCode("TEST_API_001").build());

        verify(apiConfigMapper).selectOne(any());
    }

    @Test
    void shouldReturnNullWhenConfigNotFound() {
        when(apiConfigMapper.selectOne(any())).thenReturn(null);

        var found = apiConfigRepository.selectOne(SelectOneApiConfigParam.builder().apiCode("NOT_EXISTS").build());

        assertThat(found).isNull();
    }

    private SaveApiConfigParam createSaveApiConfigParam() {
        return SaveApiConfigParam.builder()
                .groupNo("GROUP1")
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .apiDescription("Test Description")
                .requestTimeoutMs(30000L)
                .autoRetryCount(3)
                .retryIntervalMs(5000L)
                .maxQueueSize(10000)
                .build();
    }

    private ApiConfigPO createApiConfigPO() {
        return ApiConfigPO.builder()
                .id(1L)
                .groupNo("GROUP1")
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .apiDescription("Test Description")
                .status("ENABLED")
                .requestTimeoutMs(30000L)
                .autoRetryCount(3)
                .retryIntervalMs(5000L)
                .maxQueueSize(10000)
                .build();
    }

}