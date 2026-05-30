package com.apiflow.domain.config.service;

import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.api.repository.config.idto.ApiConfigIDTO;
import com.apiflow.api.repository.config.param.ApiConfigField;
import com.apiflow.api.repository.config.param.SelectOneApiConfigParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.domain.config.command.CreateApiConfigCommand;
import com.apiflow.domain.config.command.DeleteApiConfigCommand;
import com.apiflow.domain.config.command.UpdateApiConfigCommand;
import com.apiflow.domain.config.converter.ApiConfigConverter;
import com.apiflow.domain.config.model.ApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ApiConfigDomainService {

    private final ApiConfigRepository apiConfigRepository;
    private static final ApiConfigConverter CONVERTER = ApiConfigConverter.INSTANCE;

    public ApiConfig create(CreateApiConfigCommand command) {
        SelectOneApiConfigParam param = SelectOneApiConfigParam.builder()
                .condition(ConditionNode.eq(ApiConfigField.API_CODE.getFieldName(), command.getApiCode()))
                .selectFields(List.of(ApiConfigField.API_CODE))
                .build();
        ApiConfigIDTO existingDto = apiConfigRepository.selectOne(param);
        if (existingDto != null && !Boolean.TRUE.equals(existingDto.getDeleted())) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "API配置已存在");
        }

        return ApiConfig.create(command);
    }

    public ApiConfig update(ApiConfig config, UpdateApiConfigCommand command) {
        config.update(command);
        return config;
    }

    public ApiConfig enable(ApiConfig config, String operator) {
        config.enable(operator);
        return config;
    }

    public ApiConfig disable(ApiConfig config, String operator) {
        config.disable(operator);
        return config;
    }

    public ApiConfig delete(ApiConfig config, DeleteApiConfigCommand command) {
        config.delete(command);
        return config;
    }

    public ApiConfig getConfig(String apiCode) {
        SelectOneApiConfigParam param = SelectOneApiConfigParam.builder()
                .condition(ConditionNode.eq(ApiConfigField.API_CODE.getFieldName(), apiCode))
                .selectFields(List.of(ApiConfigField.values()))
                .build();
        ApiConfigIDTO configDto = apiConfigRepository.selectOne(param);
        if (configDto == null || Boolean.TRUE.equals(configDto.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }
        return CONVERTER.apiConfigIDTOToApiConfig(configDto);
    }
}
