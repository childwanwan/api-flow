package com.apiflow.interfaces.biz.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.apiflow.application.user.AuthService;
import com.apiflow.application.config.ApiConfigApplicationService;
import com.apiflow.application.config.param.DeleteApiConfigParam;
import com.apiflow.application.config.param.DisableApiConfigParam;
import com.apiflow.application.config.param.EnableApiConfigParam;
import com.apiflow.application.config.dto.ApiConfigDTO;
import com.apiflow.application.config.param.ApiConfigPageParam;
import com.apiflow.application.config.param.ListApiConfigParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.result.PageResult;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.biz.config.converter.ApiConfigConverter;
import com.apiflow.interfaces.biz.config.request.ApiConfigCreateRequest;
import com.apiflow.interfaces.biz.config.request.ApiConfigPageRequest;
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
    public Result<Void> createConfig(@RequestBody ApiConfigCreateRequest request) {
        validateApiConfigCreateRequest(request);
        log.info("Create config request: {}", request);
        apiConfigApplicationService.createConfig(ApiConfigConverter.INSTANCE.toCreateCommand(request));
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateConfig(@RequestBody ApiConfigUpdateRequest request) {
        validateApiConfigUpdateRequest(request);
        log.info("Update config request: {}", request);
        apiConfigApplicationService.updateConfig(ApiConfigConverter.INSTANCE.toUpdateCommand(request));
        return Result.success();
    }

    @GetMapping("/{apiCode}")
    public Result<ApiConfigVO> getConfig(@PathVariable String apiCode) {
        validateApiCode(apiCode);
        log.info("Get config request: apiCode={}", apiCode);
        ApiConfigDTO config = apiConfigApplicationService.getConfig(apiCode);
        return Result.success(ApiConfigConverter.INSTANCE.toVO(config));
    }

    @GetMapping("/list")
    public Result<List<ApiConfigVO>> listConfigs(ListApiConfigParam param) {
        log.info("List configs request: groupNo={}, apiCode={}, apiName={}, status={}, limit={}", param.getGroupNo(), param.getApiCode(), param.getApiName(), param.getStatus(), param.getLimit());
        List<ApiConfigDTO> configs = apiConfigApplicationService.listConfigs(param);
        List<ApiConfigVO> vos = configs.stream().map(ApiConfigConverter.INSTANCE::toVO).toList();
        return Result.success(vos);
    }

    @PostMapping("/page")
    public Result<PageResult<ApiConfigVO>> pageConfigs(@RequestBody ApiConfigPageRequest request) {
        ApiConfigPageParam pageParam = ApiConfigConverter.INSTANCE.toPageParam(request);
        PageResult<ApiConfigDTO> pageResult = apiConfigApplicationService.pageConfigs(pageParam);
        PageResult<ApiConfigVO> voPageResult = PageResult.of(
            pageResult.getRecords().stream().map(ApiConfigConverter.INSTANCE::toVO).toList(),
            pageResult.getTotal(),
            pageResult.getCurrent(),
            pageResult.getSize()
        );
        return Result.success(voPageResult);
    }

    @PostMapping("/{apiCode}/enable")
    public Result<Void> enableConfig(@PathVariable String apiCode,
                                            HttpServletRequest request) {
        log.info("Enable config request: apiCode={}", apiCode);
        EnableApiConfigParam param = EnableApiConfigParam.builder()
                .apiCode(apiCode)
                .operator(authService.getUsernameByToken(TokenUtil.getTokenFromCookie(request)))
                .build();
        apiConfigApplicationService.enableConfig(param);
        return Result.success();
    }

    @PostMapping("/{apiCode}/disable")
    public Result<Void> disableConfig(@PathVariable String apiCode,
                                             HttpServletRequest request) {
        log.info("Disable config request: apiCode={}", apiCode);
        DisableApiConfigParam param = DisableApiConfigParam.builder()
                .apiCode(apiCode)
                .operator(authService.getUsernameByToken(TokenUtil.getTokenFromCookie(request)))
                .build();
        apiConfigApplicationService.disableConfig(param);
        return Result.success();
    }

    @DeleteMapping("/{apiCode}")
    public Result<Void> deleteConfig(@PathVariable String apiCode,
                                            HttpServletRequest request) {
        log.info("Delete config request: apiCode={}", apiCode);
        DeleteApiConfigParam param = DeleteApiConfigParam.builder()
                .apiCode(apiCode)
                .operator(authService.getUsernameByToken(TokenUtil.getTokenFromCookie(request)))
                .build();
        apiConfigApplicationService.deleteConfig(param);
        return Result.success();
    }


    private void validateApiConfigCreateRequest(ApiConfigCreateRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        request.validate();
    }

    private void validateApiConfigUpdateRequest(ApiConfigUpdateRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        request.validate();
    }

    private void validateApiCode(String apiCode) {
        if (StrUtil.isBlank(apiCode)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
    }

}
