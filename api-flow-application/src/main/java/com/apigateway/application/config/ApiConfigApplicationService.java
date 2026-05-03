package com.apigateway.application.config;

import com.apigateway.application.config.command.ApiConfigCreateCommand;
import com.apigateway.application.config.command.ApiConfigDeleteCommand;
import com.apigateway.application.config.command.ApiConfigDisableCommand;
import com.apigateway.application.config.command.ApiConfigEnableCommand;
import com.apigateway.application.config.command.ApiConfigUpdateCommand;
import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.domain.config.service.ApiConfigDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiConfigApplicationService {

    private final ApiConfigDomainService apiConfigDomainService;

    public ApiConfigEntity createConfig(ApiConfigCreateCommand command) {
        return apiConfigDomainService.createConfig(
                command.getGroupNo(),
                command.getApiCode(),
                command.getApiName(),
                command.getApiDescription(),
                command.getRequestTimeoutMs(),
                command.getAutoRetryCount(),
                command.getRetryIntervalMs(),
                command.getRateLimitConfig(),
                command.getMaxQueueSize(),
                command.getFilterRules(),
                command.getPluginConfig(),
                command.getReceiptConfig(),
                command.getExtraConfig(),
                command.getOperator()
        );
    }

    public ApiConfigEntity updateConfig(ApiConfigUpdateCommand command) {
        return apiConfigDomainService.updateConfig(
                command.getApiCode(),
                command.getApiName(),
                command.getApiDescription(),
                command.getRequestTimeoutMs(),
                command.getAutoRetryCount(),
                command.getRetryIntervalMs(),
                command.getRateLimitConfig(),
                command.getMaxQueueSize(),
                command.getFilterRules(),
                command.getPluginConfig(),
                command.getReceiptConfig(),
                command.getExtraConfig(),
                command.getOperator()
        );
    }

    public ApiConfigEntity getConfig(String apiCode) {
        return apiConfigDomainService.getConfig(apiCode);
    }

    public ApiConfigEntity enableConfig(ApiConfigEnableCommand command) {
        return apiConfigDomainService.enableConfig(command.getApiCode(), command.getOperator());
    }

    public ApiConfigEntity disableConfig(ApiConfigDisableCommand command) {
        return apiConfigDomainService.disableConfig(command.getApiCode(), command.getOperator());
    }

    public ApiConfigEntity deleteConfig(ApiConfigDeleteCommand command) {
        return apiConfigDomainService.deleteConfig(command.getApiCode(), command.getOperator());
    }

}
