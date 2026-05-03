package com.apigateway.application.service;

import com.apigateway.application.config.ApiConfigApplicationService;
import com.apigateway.application.config.command.ApiConfigCreateCommand;
import com.apigateway.application.config.command.ApiConfigDeleteCommand;
import com.apigateway.application.config.command.ApiConfigDisableCommand;
import com.apigateway.application.config.command.ApiConfigEnableCommand;
import com.apigateway.application.config.command.ApiConfigUpdateCommand;
import com.apigateway.common.exception.BusinessException;
import com.apigateway.common.exception.ErrorCode;
import com.apigateway.domain.config.enums.ApiConfigStatus;
import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.domain.config.service.ApiConfigDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiConfigApplicationServiceTest {

    @Mock
    private ApiConfigDomainService apiConfigDomainService;

    @InjectMocks
    private ApiConfigApplicationService apiConfigApplicationService;

    @Test
    void shouldCreateApiConfigSuccessfully() {
        ApiConfigCreateCommand command = createApiConfigCreateCommand();
        ApiConfigEntity entity = createApiConfigEntity();
        when(apiConfigDomainService.createConfig(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(entity);

        ApiConfigEntity created = apiConfigApplicationService.createConfig(command);

        assertThat(created).isNotNull();
        verify(apiConfigDomainService).createConfig(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
        );
    }

    @Test
    void shouldUpdateApiConfigSuccessfully() {
        ApiConfigUpdateCommand command = createApiConfigUpdateCommand();
        ApiConfigEntity entity = createApiConfigEntity();
        when(apiConfigDomainService.updateConfig(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(entity);

        ApiConfigEntity updated = apiConfigApplicationService.updateConfig(command);

        assertThat(updated).isNotNull();
    }

    @Test
    void shouldEnableApiConfig() {
        ApiConfigEnableCommand command = ApiConfigEnableCommand.builder()
                .apiCode("TEST_API_001")
                .operator("admin")
                .build();
        ApiConfigEntity entity = createApiConfigEntity();
        when(apiConfigDomainService.enableConfig(any(), any())).thenReturn(entity);

        ApiConfigEntity enabled = apiConfigApplicationService.enableConfig(command);

        assertThat(enabled).isNotNull();
    }

    @Test
    void shouldDisableApiConfig() {
        ApiConfigDisableCommand command = ApiConfigDisableCommand.builder()
                .apiCode("TEST_API_001")
                .operator("admin")
                .build();
        ApiConfigEntity entity = createApiConfigEntity();
        when(apiConfigDomainService.disableConfig(any(), any())).thenReturn(entity);

        ApiConfigEntity disabled = apiConfigApplicationService.disableConfig(command);

        assertThat(disabled).isNotNull();
    }

    @Test
    void shouldDeleteApiConfig() {
        ApiConfigDeleteCommand command = ApiConfigDeleteCommand.builder()
                .apiCode("TEST_API_001")
                .operator("admin")
                .build();
        ApiConfigEntity entity = createApiConfigEntity();
        when(apiConfigDomainService.deleteConfig(any(), any())).thenReturn(entity);

        apiConfigApplicationService.deleteConfig(command);

        verify(apiConfigDomainService).deleteConfig(any(), any());
    }

    private ApiConfigCreateCommand createApiConfigCreateCommand() {
        return ApiConfigCreateCommand.builder()
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .requestTimeoutMs(30000L)
                .autoRetryCount(3)
                .retryIntervalMs(5000L)
                .operator("admin")
                .build();
    }

    private ApiConfigUpdateCommand createApiConfigUpdateCommand() {
        return ApiConfigUpdateCommand.builder()
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .requestTimeoutMs(30000L)
                .autoRetryCount(3)
                .retryIntervalMs(5000L)
                .operator("admin")
                .build();
    }

    private ApiConfigEntity createApiConfigEntity() {
        return ApiConfigEntity.builder()
                .id(1L)
                .apiCode("TEST_API_001")
                .apiName("Test API")
                .status(ApiConfigStatus.ENABLED)
                .requestTimeoutMs(30000L)
                .autoRetryCount(3)
                .retryIntervalMs(5000L)
                .build();
    }

}
