package com.flow.domain.config.service;

import com.flow.api.repository.config.dto.ApiConfigIDTO;
import com.flow.api.repository.config.param.SelectOneApiConfigParam;
import com.flow.api.repository.config.ApiConfigRepository;
import com.flow.common.constant.SystemConstant;
import com.flow.common.exception.BusinessException;
import com.flow.common.exception.ErrorCode;
import com.flow.domain.config.converter.ApiConfigConverter;
import com.flow.domain.config.model.ApiConfigDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiConfigDomainService {

    private final ApiConfigRepository apiConfigRepository;
    private static final ApiConfigConverter API_CONFIG_CONVERTER = ApiConfigConverter.INSTANCE;

    public ApiConfigDomainService(ApiConfigRepository apiConfigRepository) {
        this.apiConfigRepository = apiConfigRepository;
    }

    public ApiConfigDO createConfig(String groupNo, String apiCode, String apiName, String apiDescription,
                                    Long requestTimeoutMs, Integer autoRetryCount, Long retryIntervalMs,
                                    Integer maxQueueSize,
                                    String operator) {
        SelectOneApiConfigParam param = SelectOneApiConfigParam.builder().apiCode(apiCode).build();
        ApiConfigIDTO existingDto = apiConfigRepository.selectOne(param);
        if (existingDto != null && !Boolean.TRUE.equals(existingDto.getDeleted())) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "API配置已存在");
        }

        long now = System.currentTimeMillis();
        ApiConfigDO config = ApiConfigDO.builder()
                .groupNo(groupNo)
                .apiCode(apiCode)
                .apiName(apiName)
                .apiDescription(apiDescription)
                .status("ENABLED")
                .requestTimeoutMs(requestTimeoutMs == null ? SystemConstant.DEFAULT_REQUEST_TIMEOUT_MS : requestTimeoutMs)
                .autoRetryCount(autoRetryCount == null ? SystemConstant.DEFAULT_AUTO_RETRY_COUNT : autoRetryCount)
                .retryIntervalMs(retryIntervalMs == null ? SystemConstant.DEFAULT_RETRY_INTERVAL_MS : retryIntervalMs)
                .maxQueueSize(maxQueueSize == null ? 100000 : maxQueueSize)
                .createTimeMs(now)
                .updateTimeMs(now)
                .createOperator(operator)
                .updateOperator(operator)
                .deleted(false)
                .version(0)
                .build();

        ApiConfigIDTO savedDto = apiConfigRepository.save(API_CONFIG_CONVERTER.apiConfigDOToSaveApiConfigParam(config));
        return API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(savedDto);
    }

    public ApiConfigDO updateConfig(String apiCode, String apiName, String apiDescription,
                                    Long requestTimeoutMs, Integer autoRetryCount, Long retryIntervalMs,
                                    Integer maxQueueSize,
                                    String operator) {
        SelectOneApiConfigParam param = SelectOneApiConfigParam.builder().apiCode(apiCode).build();
        ApiConfigIDTO configDto = apiConfigRepository.selectOne(param);
        if (configDto == null || Boolean.TRUE.equals(configDto.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }

        ApiConfigDO config = API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(configDto);

        if (apiName != null) {
            config.setApiName(apiName);
        }
        if (apiDescription != null) {
            config.setApiDescription(apiDescription);
        }
        if (requestTimeoutMs != null) {
            config.setRequestTimeoutMs(requestTimeoutMs);
        }
        if (autoRetryCount != null) {
            config.setAutoRetryCount(autoRetryCount);
        }
        if (retryIntervalMs != null) {
            config.setRetryIntervalMs(retryIntervalMs);
        }
        if (maxQueueSize != null) {
            config.setMaxQueueSize(maxQueueSize);
        }
        config.setUpdateOperator(operator);
        config.setUpdateTimeMs(System.currentTimeMillis());

        ApiConfigIDTO updatedDto = apiConfigRepository.update(API_CONFIG_CONVERTER.apiConfigDOToUpdateApiConfigParam(config));
        return API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(updatedDto);
    }

    public ApiConfigDO enableConfig(String apiCode, String operator) {
        SelectOneApiConfigParam param = SelectOneApiConfigParam.builder().apiCode(apiCode).build();
        ApiConfigIDTO configDto = apiConfigRepository.selectOne(param);
        if (configDto == null || Boolean.TRUE.equals(configDto.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }

        ApiConfigDO config = API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(configDto);
        config.enable();
        config.setUpdateOperator(operator);

        ApiConfigIDTO updatedDto = apiConfigRepository.update(API_CONFIG_CONVERTER.apiConfigDOToUpdateApiConfigParam(config));
        return API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(updatedDto);
    }

    public ApiConfigDO disableConfig(String apiCode, String operator) {
        SelectOneApiConfigParam param = SelectOneApiConfigParam.builder().apiCode(apiCode).build();
        ApiConfigIDTO configDto = apiConfigRepository.selectOne(param);
        if (configDto == null || Boolean.TRUE.equals(configDto.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }

        ApiConfigDO config = API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(configDto);
        config.disable();
        config.setUpdateOperator(operator);

        ApiConfigIDTO updatedDto = apiConfigRepository.update(API_CONFIG_CONVERTER.apiConfigDOToUpdateApiConfigParam(config));
        return API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(updatedDto);
    }

    public ApiConfigDO deleteConfig(String apiCode, String operator) {
        SelectOneApiConfigParam param = SelectOneApiConfigParam.builder().apiCode(apiCode).build();
        ApiConfigIDTO configDto = apiConfigRepository.selectOne(param);
        if (configDto == null || Boolean.TRUE.equals(configDto.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }

        ApiConfigDO config = API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(configDto);
        config.setDeleted(true);
        config.setUpdateOperator(operator);
        config.setUpdateTimeMs(System.currentTimeMillis());

        ApiConfigIDTO updatedDto = apiConfigRepository.update(API_CONFIG_CONVERTER.apiConfigDOToUpdateApiConfigParam(config));
        return API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(updatedDto);
    }

    public ApiConfigDO getConfig(String apiCode) {
        SelectOneApiConfigParam param = SelectOneApiConfigParam.builder().apiCode(apiCode).build();
        ApiConfigIDTO configDto = apiConfigRepository.selectOne(param);
        if (configDto == null || Boolean.TRUE.equals(configDto.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND);
        }
        return API_CONFIG_CONVERTER.apiConfigIDTOToApiConfigDO(configDto);
    }

}