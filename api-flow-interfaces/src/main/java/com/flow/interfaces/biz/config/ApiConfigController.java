package com.flow.interfaces.biz.config;

import com.flow.application.auth.AuthService;
import com.flow.application.config.ApiConfigApplicationService;
import com.flow.application.config.command.ApiConfigDeleteCommand;
import com.flow.application.config.command.ApiConfigDisableCommand;
import com.flow.application.config.command.ApiConfigEnableCommand;
import com.flow.common.result.Result;
import com.flow.domain.config.model.ApiConfigDO;
import com.flow.interfaces.biz.config.converter.ApiConfigConverter;
import com.flow.interfaces.biz.config.dto.ApiConfigCreateRequest;
import com.flow.interfaces.biz.config.dto.ApiConfigUpdateRequest;
import com.flow.interfaces.biz.config.dto.vo.ApiConfigVO;
import com.flow.interfaces.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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
        ApiConfigDO config = apiConfigApplicationService.createConfig(ApiConfigConverter.INSTANCE.toCreateCommand(request));
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @PutMapping("/update")
    public Result<ApiConfigVO> updateConfig(@RequestBody ApiConfigUpdateRequest request) {
        validateApiConfigUpdateRequest(request);
        log.info("Update config request: {}", request);
        ApiConfigDO config = apiConfigApplicationService.updateConfig(ApiConfigConverter.INSTANCE.toUpdateCommand(request));
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @GetMapping("/{apiCode}")
    public Result<ApiConfigVO> getConfig(@PathVariable String apiCode) {
        validateApiCode(apiCode);
        log.info("Get config request: apiCode={}", apiCode);
        ApiConfigDO config = apiConfigApplicationService.getConfig(apiCode);
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @PostMapping("/{apiCode}/enable")
    public Result<ApiConfigVO> enableConfig(@PathVariable String apiCode,
                                            HttpServletRequest request) {
        log.info("Enable config request: apiCode={}", apiCode);
        ApiConfigEnableCommand command = ApiConfigEnableCommand.builder()
                .apiCode(apiCode)
                .operator(authService.getUsernameByToken(TokenUtil.getTokenFromCookie(request)))
                .build();
        ApiConfigDO config = apiConfigApplicationService.enableConfig(command);
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @PostMapping("/{apiCode}/disable")
    public Result<ApiConfigVO> disableConfig(@PathVariable String apiCode,
                                             HttpServletRequest request) {
        log.info("Disable config request: apiCode={}", apiCode);
        ApiConfigDisableCommand command = ApiConfigDisableCommand.builder()
                .apiCode(apiCode)
                .operator(authService.getUsernameByToken(TokenUtil.getTokenFromCookie(request)))
                .build();
        ApiConfigDO config = apiConfigApplicationService.disableConfig(command);
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @DeleteMapping("/{apiCode}")
    public Result<ApiConfigVO> deleteConfig(@PathVariable String apiCode,
                                            HttpServletRequest request) {
        log.info("Delete config request: apiCode={}", apiCode);
        ApiConfigDeleteCommand command = ApiConfigDeleteCommand.builder()
                .apiCode(apiCode)
                .operator(authService.getUsernameByToken(TokenUtil.getTokenFromCookie(request)))
                .build();
        ApiConfigDO config = apiConfigApplicationService.deleteConfig(command);
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }


    private void validateApiConfigCreateRequest(ApiConfigCreateRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalArgumentException("request cannot be null");
        }
        request.validate();
    }

    private void validateApiConfigUpdateRequest(ApiConfigUpdateRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalArgumentException("request cannot be null");
        }
        request.validate();
    }

    private void validateApiCode(String apiCode) {
        if (StringUtils.isBlank(apiCode)) {
            throw new IllegalArgumentException("apiCode cannot be null");
        }
        // todo 完善校验
    }

}