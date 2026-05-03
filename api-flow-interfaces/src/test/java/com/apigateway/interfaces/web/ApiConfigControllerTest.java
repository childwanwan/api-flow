package com.apigateway.interfaces.web;

import com.apigateway.application.config.ApiConfigApplicationService;
import com.apigateway.application.config.command.ApiConfigCreateCommand;
import com.apigateway.application.config.command.ApiConfigDeleteCommand;
import com.apigateway.application.config.command.ApiConfigDisableCommand;
import com.apigateway.application.config.command.ApiConfigEnableCommand;
import com.apigateway.application.config.command.ApiConfigUpdateCommand;
import com.apigateway.common.result.Result;
import com.apigateway.domain.config.enums.ApiConfigStatus;
import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.interfaces.config.ApiConfigController;
import com.apigateway.interfaces.config.dto.ApiConfigCreateRequest;
import com.apigateway.interfaces.config.dto.ApiConfigUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ApiConfigControllerTest {

    @Mock
    private ApiConfigApplicationService apiConfigApplicationService;

    private ApiConfigController apiConfigController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        apiConfigController = new ApiConfigController(apiConfigApplicationService);
    }

    @Test
    void shouldCreateConfig() {
        ApiConfigCreateRequest request = ApiConfigCreateRequest.builder()
                .groupNo("GROUP_001")
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .requestTimeoutMs(30000L)
                .autoRetryCount(3)
                .operator("admin")
                .build();

        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .groupNo("GROUP_001")
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .status(ApiConfigStatus.ENABLED)
                .build();

        when(apiConfigApplicationService.createConfig(any(ApiConfigCreateCommand.class))).thenReturn(entity);

        Result<ApiConfigEntity> result = apiConfigController.createConfig(request);

        assertTrue(result.isSuccess());
        assertEquals("TEST_API_001", result.getData().getApiCode());
        verify(apiConfigApplicationService).createConfig(any(ApiConfigCreateCommand.class));
    }

    @Test
    void shouldUpdateConfig() {
        ApiConfigUpdateRequest request = ApiConfigUpdateRequest.builder()
                .apiCode("TEST_API_001")
                .apiName("Updated API")
                .requestTimeoutMs(60000L)
                .operator("admin")
                .build();

        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .apiName("Updated API")
                .status(ApiConfigStatus.ENABLED)
                .build();

        when(apiConfigApplicationService.updateConfig(any(ApiConfigUpdateCommand.class))).thenReturn(entity);

        Result<ApiConfigEntity> result = apiConfigController.updateConfig(request);

        assertTrue(result.isSuccess());
        assertEquals("Updated API", result.getData().getApiName());
        verify(apiConfigApplicationService).updateConfig(any(ApiConfigUpdateCommand.class));
    }

    @Test
    void shouldGetConfig() {
        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .status(ApiConfigStatus.ENABLED)
                .build();

        when(apiConfigApplicationService.getConfig("TEST_API_001")).thenReturn(entity);

        Result<ApiConfigEntity> result = apiConfigController.getConfig("TEST_API_001");

        assertTrue(result.isSuccess());
        assertEquals("TEST_API_001", result.getData().getApiCode());
        verify(apiConfigApplicationService).getConfig("TEST_API_001");
    }

    @Test
    void shouldEnableConfig() {
        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .status(ApiConfigStatus.ENABLED)
                .build();

        when(apiConfigApplicationService.enableConfig(any(ApiConfigEnableCommand.class))).thenReturn(entity);

        Result<ApiConfigEntity> result = apiConfigController.enableConfig("TEST_API_001", "admin");

        assertTrue(result.isSuccess());
        assertEquals(ApiConfigStatus.ENABLED, result.getData().getStatus());
        verify(apiConfigApplicationService).enableConfig(any(ApiConfigEnableCommand.class));
    }

    @Test
    void shouldDisableConfig() {
        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .status(ApiConfigStatus.DISABLED)
                .build();

        when(apiConfigApplicationService.disableConfig(any(ApiConfigDisableCommand.class))).thenReturn(entity);

        Result<ApiConfigEntity> result = apiConfigController.disableConfig("TEST_API_001", "admin");

        assertTrue(result.isSuccess());
        assertEquals(ApiConfigStatus.DISABLED, result.getData().getStatus());
        verify(apiConfigApplicationService).disableConfig(any(ApiConfigDisableCommand.class));
    }

    @Test
    void shouldDeleteConfig() {
        ApiConfigEntity entity = ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .status(ApiConfigStatus.DISABLED)
                .build();

        when(apiConfigApplicationService.deleteConfig(any(ApiConfigDeleteCommand.class))).thenReturn(entity);

        Result<ApiConfigEntity> result = apiConfigController.deleteConfig("TEST_API_001", "admin");

        assertTrue(result.isSuccess());
        verify(apiConfigApplicationService).deleteConfig(any(ApiConfigDeleteCommand.class));
    }

}