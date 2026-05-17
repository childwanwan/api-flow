package com.apiflow.interfaces.biz.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.apiflow.application.user.AuthService;
import com.apiflow.application.config.ApiConfigApplicationService;
import com.apiflow.application.config.command.ApiConfigDeleteCommand;
import com.apiflow.application.config.command.ApiConfigDisableCommand;
import com.apiflow.application.config.command.ApiConfigEnableCommand;
import com.apiflow.application.config.dto.ApiConfigDTO;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.annotation.OpLog;
import com.apiflow.interfaces.biz.config.converter.ApiConfigConverter;
import com.apiflow.interfaces.biz.config.request.ApiConfigCreateRequest;
import com.apiflow.interfaces.biz.config.request.ApiConfigUpdateRequest;
import com.apiflow.interfaces.biz.config.vo.ApiConfigVO;
import com.apiflow.interfaces.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/config")
public class ApiConfigController {

    private final ApiConfigApplicationService apiConfigApplicationService;
    private final AuthService authService;

    @PostMapping("/create")
    public Result<ApiConfigVO> createConfig(@RequestBody ApiConfigCreateRequest request) {
        validateApiConfigCreateRequest(request);
        log.info("Create config request: {}", request);
        ApiConfigDTO config = apiConfigApplicationService.createConfig(ApiConfigConverter.INSTANCE.toCreateCommand(request));
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @PutMapping("/update")
    public Result<ApiConfigVO> updateConfig(@RequestBody ApiConfigUpdateRequest request) {
        validateApiConfigUpdateRequest(request);
        log.info("Update config request: {}", request);
        ApiConfigDTO config = apiConfigApplicationService.updateConfig(ApiConfigConverter.INSTANCE.toUpdateCommand(request));
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @GetMapping("/{apiCode}")
    public Result<ApiConfigVO> getConfig(@PathVariable String apiCode) {
        validateApiCode(apiCode);
        log.info("Get config request: apiCode={}", apiCode);
        ApiConfigDTO config = apiConfigApplicationService.getConfig(apiCode);
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @GetMapping("/list")
    public Result<List<ApiConfigVO>> listConfigs(
            @RequestParam(required = false) String groupNo,
            @RequestParam(required = false) String apiCode,
            @RequestParam(required = false) String apiName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer limit) {
        log.info("List configs request: groupNo={}, apiCode={}, apiName={}, status={}, limit={}", groupNo, apiCode, apiName, status, limit);
        List<ApiConfigDTO> configs = apiConfigApplicationService.listConfigs(groupNo, apiCode, apiName, status, limit);
        List<ApiConfigVO> vos = configs.stream().map(ApiConfigConverter.INSTANCE::toVO).toList();
        return Result.success(vos);
    }

    @PostMapping("/{apiCode}/enable")
    @OpLog(module = "API配置", operation = "启用", detail = "启用API配置")
    public Result<ApiConfigVO> enableConfig(@PathVariable String apiCode,
                                            HttpServletRequest request) {
        log.info("Enable config request: apiCode={}", apiCode);
        ApiConfigEnableCommand command = ApiConfigEnableCommand.builder()
                .apiCode(apiCode)
                .operator(authService.getUsernameByToken(TokenUtil.getTokenFromCookie(request)))
                .build();
        ApiConfigDTO config = apiConfigApplicationService.enableConfig(command);
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @PostMapping("/{apiCode}/disable")
    @OpLog(module = "API配置", operation = "禁用", detail = "禁用API配置")
    public Result<ApiConfigVO> disableConfig(@PathVariable String apiCode,
                                             HttpServletRequest request) {
        log.info("Disable config request: apiCode={}", apiCode);
        ApiConfigDisableCommand command = ApiConfigDisableCommand.builder()
                .apiCode(apiCode)
                .operator(authService.getUsernameByToken(TokenUtil.getTokenFromCookie(request)))
                .build();
        ApiConfigDTO config = apiConfigApplicationService.disableConfig(command);
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @DeleteMapping("/{apiCode}")
    @OpLog(module = "API配置", operation = "删除", detail = "删除API配置")
    public Result<String> deleteConfig(@PathVariable String apiCode,
                                            HttpServletRequest request) {
        log.info("Delete config request: apiCode={}", apiCode);
        ApiConfigDeleteCommand command = ApiConfigDeleteCommand.builder()
                .apiCode(apiCode)
                .operator(authService.getUsernameByToken(TokenUtil.getTokenFromCookie(request)))
                .build();
        apiConfigApplicationService.deleteConfig(command);
        return Result.success(apiCode);
    }


    private void validateApiConfigCreateRequest(ApiConfigCreateRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new IllegalArgumentException("request cannot be null");
        }
        request.validate();
    }

    private void validateApiConfigUpdateRequest(ApiConfigUpdateRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new IllegalArgumentException("request cannot be null");
        }
        request.validate();
    }

    private void validateApiCode(String apiCode) {
        if (StrUtil.isBlank(apiCode)) {
            throw new IllegalArgumentException("apiCode cannot be blank");
        }
    }

}
