package com.flow.interfaces.web;

import com.flow.application.auth.AuthService;
import com.flow.application.config.ApiConfigApplicationService;
import com.flow.application.config.command.ApiConfigCreateCommand;
import com.flow.application.config.command.ApiConfigUpdateCommand;
import com.flow.common.result.Result;
import com.flow.domain.config.model.ApiConfigDO;
import com.flow.interfaces.biz.config.ApiConfigController;
import com.flow.interfaces.biz.config.converter.ApiConfigConverter;
import com.flow.interfaces.biz.config.dto.ApiConfigCreateRequest;
import com.flow.interfaces.biz.config.dto.ApiConfigUpdateRequest;
import com.flow.interfaces.biz.config.dto.request.ApiConfigRateLimitConfigRequest;
import com.flow.interfaces.biz.config.dto.request.ApiConfigRateLimitRuleRequest;
import com.flow.interfaces.biz.config.dto.vo.ApiConfigVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ApiConfigControllerTest {

    @Mock
    private ApiConfigApplicationService apiConfigApplicationService;

    @Mock
    private AuthService authService;

    private ApiConfigController apiConfigController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        apiConfigController = new ApiConfigController(apiConfigApplicationService, authService);
    }

    @Test
    void shouldCreateConfig() {
        ApiConfigCreateRequest request = ApiConfigCreateRequest.builder()
                .groupNo("GROUP_001")
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .requestTimeoutMs(30000L)
                .autoRetryCount(3)
                .rateLimitConfig(ApiConfigRateLimitConfigRequest.builder()
                        .enabled(true)
                        .rules(Collections.singletonList(ApiConfigRateLimitRuleRequest.builder()
                                .name("rule1")
                                .maxRequests(100L)
                                .timeWindowMs(60000L)
                                .build()))
                        .build())
                .operator("admin")
                .build();

        ApiConfigDO entity = ApiConfigDO.builder()
                .id(1L)
                .groupNo("GROUP_001")
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .status("ENABLED")
                .build();

        when(apiConfigApplicationService.createConfig(any())).thenReturn(entity);

        Result<ApiConfigVO> result = apiConfigController.createConfig(request);

        assertTrue(result.isSuccess());
        assertEquals("TEST_API_001", result.getData().getApiCode());
        verify(apiConfigApplicationService).createConfig(any());
    }

    @Test
    void shouldUpdateConfig() {
        ApiConfigUpdateRequest request = ApiConfigUpdateRequest.builder()
                .apiCode("TEST_API_001")
                .apiName("Updated API")
                .requestTimeoutMs(60000L)
                .operator("admin")
                .build();

        ApiConfigDO entity = ApiConfigDO.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .apiName("Updated API")
                .status("ENABLED")
                .build();

        when(apiConfigApplicationService.updateConfig(any())).thenReturn(entity);

        Result<ApiConfigVO> result = apiConfigController.updateConfig(request);

        assertTrue(result.isSuccess());
        assertEquals("Updated API", result.getData().getApiName());
        verify(apiConfigApplicationService).updateConfig(any());
    }

    @Test
    void shouldGetConfig() {
        ApiConfigDO entity = ApiConfigDO.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .status("ENABLED")
                .build();

        when(apiConfigApplicationService.getConfig(any())).thenReturn(entity);

        Result<ApiConfigVO> result = apiConfigController.getConfig("TEST_API_001");

        assertTrue(result.isSuccess());
        assertEquals("TEST_API_001", result.getData().getApiCode());
        verify(apiConfigApplicationService).getConfig("TEST_API_001");
    }

    @Test
    void shouldEnableConfig() {
        ApiConfigDO entity = ApiConfigDO.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .status("ENABLED")
                .build();

        when(apiConfigApplicationService.enableConfig(any())).thenReturn(entity);

        Result<ApiConfigVO> result = apiConfigController.enableConfig("TEST_API_001", any());

        assertTrue(result.isSuccess());
        assertEquals("ENABLED", result.getData().getStatus());
        verify(apiConfigApplicationService).enableConfig(any());
    }

    @Test
    void shouldDisableConfig() {
        ApiConfigDO entity = ApiConfigDO.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .status("DISABLED")
                .build();

        when(apiConfigApplicationService.disableConfig(any())).thenReturn(entity);

        Result<ApiConfigVO> result = apiConfigController.disableConfig("TEST_API_001", any());

        assertTrue(result.isSuccess());
        assertEquals("DISABLED", result.getData().getStatus());
        verify(apiConfigApplicationService).disableConfig(any());
    }

    @Test
    void shouldDeleteConfig() {
        ApiConfigDO entity = ApiConfigDO.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .status("DISABLED")
                .build();

        when(apiConfigApplicationService.deleteConfig(any())).thenReturn(entity);

        Result<ApiConfigVO> result = apiConfigController.deleteConfig("TEST_API_001", any());

        assertTrue(result.isSuccess());
        verify(apiConfigApplicationService).deleteConfig(any());
    }

}