package com.apigateway.interfaces.config;

import com.apigateway.application.config.ApiConfigApplicationService;
import com.apigateway.application.config.command.ApiConfigCreateCommand;
import com.apigateway.application.config.command.ApiConfigDeleteCommand;
import com.apigateway.application.config.command.ApiConfigDisableCommand;
import com.apigateway.application.config.command.ApiConfigEnableCommand;
import com.apigateway.application.config.command.ApiConfigUpdateCommand;
import com.apigateway.common.result.Result;
import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.interfaces.config.dto.ApiConfigCreateRequest;
import com.apigateway.interfaces.config.dto.ApiConfigUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
public class ApiConfigController {

    private final ApiConfigApplicationService apiConfigApplicationService;

    @PostMapping
    public Result<ApiConfigEntity> createConfig(@Valid @RequestBody ApiConfigCreateRequest request) {
        log.info("Create config request: {}", request);
        ApiConfigCreateCommand command = ApiConfigCreateCommand.builder()
                .groupNo(request.getGroupNo())
                .apiCode(request.getApiCode())
                .apiName(request.getApiName())
                .apiDescription(request.getApiDescription())
                .requestTimeoutMs(request.getRequestTimeoutMs())
                .autoRetryCount(request.getAutoRetryCount())
                .retryIntervalMs(request.getRetryIntervalMs())
                .rateLimitConfig(request.getRateLimitConfig())
                .maxQueueSize(request.getMaxQueueSize())
                .filterRules(request.getFilterRules())
                .pluginConfig(request.getPluginConfig())
                .receiptConfig(request.getReceiptConfig())
                .extraConfig(request.getExtraConfig())
                .operator(request.getOperator())
                .build();
        ApiConfigEntity config = apiConfigApplicationService.createConfig(command);
        return Result.success(config);
    }

    @PutMapping
    public Result<ApiConfigEntity> updateConfig(@Valid @RequestBody ApiConfigUpdateRequest request) {
        log.info("Update config request: {}", request);
        ApiConfigUpdateCommand command = ApiConfigUpdateCommand.builder()
                .apiCode(request.getApiCode())
                .apiName(request.getApiName())
                .apiDescription(request.getApiDescription())
                .requestTimeoutMs(request.getRequestTimeoutMs())
                .autoRetryCount(request.getAutoRetryCount())
                .retryIntervalMs(request.getRetryIntervalMs())
                .rateLimitConfig(request.getRateLimitConfig())
                .maxQueueSize(request.getMaxQueueSize())
                .filterRules(request.getFilterRules())
                .pluginConfig(request.getPluginConfig())
                .receiptConfig(request.getReceiptConfig())
                .extraConfig(request.getExtraConfig())
                .operator(request.getOperator())
                .build();
        ApiConfigEntity config = apiConfigApplicationService.updateConfig(command);
        return Result.success(config);
    }

    @GetMapping("/{apiCode}")
    public Result<ApiConfigEntity> getConfig(@PathVariable String apiCode) {
        log.info("Get config request: apiCode={}", apiCode);
        ApiConfigEntity config = apiConfigApplicationService.getConfig(apiCode);
        return Result.success(config);
    }

    @PostMapping("/{apiCode}/enable")
    public Result<ApiConfigEntity> enableConfig(@PathVariable String apiCode,
                                                 @RequestParam(required = false) String operator) {
        log.info("Enable config request: apiCode={}, operator={}", apiCode, operator);
        ApiConfigEnableCommand command = ApiConfigEnableCommand.builder()
                .apiCode(apiCode)
                .operator(operator)
                .build();
        ApiConfigEntity config = apiConfigApplicationService.enableConfig(command);
        return Result.success(config);
    }

    @PostMapping("/{apiCode}/disable")
    public Result<ApiConfigEntity> disableConfig(@PathVariable String apiCode,
                                                  @RequestParam(required = false) String operator) {
        log.info("Disable config request: apiCode={}, operator={}", apiCode, operator);
        ApiConfigDisableCommand command = ApiConfigDisableCommand.builder()
                .apiCode(apiCode)
                .operator(operator)
                .build();
        ApiConfigEntity config = apiConfigApplicationService.disableConfig(command);
        return Result.success(config);
    }

    @DeleteMapping("/{apiCode}")
    public Result<ApiConfigEntity> deleteConfig(@PathVariable String apiCode,
                                                 @RequestParam(required = false) String operator) {
        log.info("Delete config request: apiCode={}, operator={}", apiCode, operator);
        ApiConfigDeleteCommand command = ApiConfigDeleteCommand.builder()
                .apiCode(apiCode)
                .operator(operator)
                .build();
        ApiConfigEntity config = apiConfigApplicationService.deleteConfig(command);
        return Result.success(config);
    }

}
