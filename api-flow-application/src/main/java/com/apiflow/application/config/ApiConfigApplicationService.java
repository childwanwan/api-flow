package com.apiflow.application.config;

import cn.hutool.core.collection.CollUtil;
import com.apiflow.api.async.AsyncGateway;
import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.api.repository.config.idto.ApiConfigIDTO;
import com.apiflow.api.repository.config.param.SelectApiConfigParam;
import com.apiflow.api.repository.config.param.SelectPageApiConfigParam;
import com.apiflow.api.repository.group.ApiGroupRepository;
import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.ApiGroupField;
import com.apiflow.api.repository.group.param.SelectApiGroupParam;
import com.apiflow.application.LockHelper;
import com.apiflow.application.config.converter.ApiConfigConverter;
import com.apiflow.application.config.dto.ApiConfigDTO;
import com.apiflow.application.config.param.*;
import com.apiflow.application.shared.event.DomainEventConverterFacade;
import com.apiflow.common.constant.LockPrefix;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.result.PageResult;
import com.apiflow.common.util.ApiStreamUtil;
import com.apiflow.domain.config.command.CreateApiConfigCommand;
import com.apiflow.domain.config.command.DeleteApiConfigCommand;
import com.apiflow.domain.config.command.UpdateApiConfigCommand;
import com.apiflow.domain.config.model.ApiConfig;
import com.apiflow.domain.config.service.ApiConfigDomainService;
import com.apiflow.domain.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiConfigApplicationService {

    private static final String LOCK_PREFIX = LockPrefix.API_CONFIG;
    private static final ApiConfigConverter CONVERTER = ApiConfigConverter.INSTANCE;

    private final ApiConfigDomainService apiConfigDomainService;
    private final ApiConfigRepository apiConfigRepository;
    private final ApiGroupRepository apiGroupRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final DomainEventConverterFacade domainEventConverterFacade;
    private final LockHelper lockHelper;
    private final AsyncGateway asyncGateway;

    public void createConfig(CreateApiConfigParam param) {
        lockHelper.executeVoid(LOCK_PREFIX, param.getApiCode(), () -> {
            CreateApiConfigCommand domainCommand = CONVERTER.toCreateDomainCommand(param);
            ApiConfig config = apiConfigDomainService.create(domainCommand);
            apiConfigRepository.save(CONVERTER.apiConfigToSaveApiConfigParam(config));
            domainEventPublisher.publishAll(domainEventConverterFacade.convert(config));
        });
    }

    public void updateConfig(UpdateApiConfigParam param) {
        lockHelper.executeVoid(LOCK_PREFIX, param.getApiCode(), () -> {
            ApiConfig beforeConfig = apiConfigDomainService.getConfig(param.getApiCode());
            UpdateApiConfigCommand domainCommand = CONVERTER.toUpdateDomainCommand(param);
            ApiConfig config = apiConfigDomainService.update(beforeConfig, domainCommand);
            apiConfigRepository.update(CONVERTER.apiConfigToUpdateApiConfigParam(config));
            domainEventPublisher.publishAll(domainEventConverterFacade.convert(config));
        });
    }

    public ApiConfigDTO getConfig(String apiCode) {
        ApiConfig config = apiConfigDomainService.getConfig(apiCode);
        return CONVERTER.toDTO(config);
    }

    public List<ApiConfigDTO> listConfigs(ListApiConfigParam param) {
        SelectApiConfigParam queryParam = CONVERTER.buildSelectApiConfigParam(param);
        List<ApiConfigIDTO> configs = apiConfigRepository.selectList(queryParam);
        return configs.stream().map(CONVERTER::idtoToDTO).toList();
    }

    public PageResult<ApiConfigDTO> pageConfigs(ApiConfigPageParam param) {
        SelectPageApiConfigParam queryParam = CONVERTER.apiConfigPageParam2SelectPageApiConfigParam(param);
        PageResult<ApiConfigIDTO> page = apiConfigRepository.selectPage(queryParam);
        CompletableFuture<Map<String, ApiGroupIDTO>> groupMapFeature = asyncGateway.supplyAsync(() -> queryGroupMap(page.getRecords()));
        List<ApiConfigDTO> dtoList = CompletableFuture.allOf(groupMapFeature)
                .thenApply(v -> CONVERTER.idtosToDTOs(page.getRecords(), groupMapFeature.join()))
                .join();
        return PageResult.of(dtoList, page.getTotal(), param.getEffectiveCurrent(), param.getEffectiveSize());
    }

    public void enableConfig(EnableApiConfigParam param) {
        lockHelper.executeVoid(LOCK_PREFIX, param.getApiCode(), () -> {
            ApiConfig config = apiConfigDomainService.getConfig(param.getApiCode());
            ApiConfig updatedConfig = apiConfigDomainService.enable(config, param.getOperator());
            apiConfigRepository.update(CONVERTER.apiConfigToUpdateApiConfigParam(updatedConfig));
            domainEventPublisher.publishAll(domainEventConverterFacade.convert(updatedConfig));
        });
    }

    public void disableConfig(DisableApiConfigParam param) {
        lockHelper.executeVoid(LOCK_PREFIX, param.getApiCode(), () -> {
            ApiConfig config = apiConfigDomainService.getConfig(param.getApiCode());
            ApiConfig updatedConfig = apiConfigDomainService.disable(config, param.getOperator());
            apiConfigRepository.update(CONVERTER.apiConfigToUpdateApiConfigParam(updatedConfig));
            domainEventPublisher.publishAll(domainEventConverterFacade.convert(updatedConfig));
        });
    }

    public void deleteConfig(DeleteApiConfigParam param) {
        lockHelper.executeVoid(LOCK_PREFIX, param.getApiCode(), () -> {
            ApiConfig config = apiConfigDomainService.getConfig(param.getApiCode());
            DeleteApiConfigCommand domainCommand = CONVERTER.toDeleteDomainCommand(param);
            ApiConfig updatedConfig = apiConfigDomainService.delete(config, domainCommand);
            apiConfigRepository.deleteList(CollUtil.newArrayList(config.getId()));
            domainEventPublisher.publishAll(domainEventConverterFacade.convert(updatedConfig));
        });
    }

    private Map<String, ApiGroupIDTO> queryGroupMap(List<ApiConfigIDTO> idtos) {
        List<String> groupNoList = ApiStreamUtil.mapFilterBlankDistinct(idtos, ApiConfigIDTO::getGroupNo);
        if (CollUtil.isEmpty(groupNoList)) {
            return Map.of();
        }
        SelectApiGroupParam groupQueryParam = SelectApiGroupParam.builder()
                .selectFields(List.of(ApiGroupField.GROUP_NO, ApiGroupField.GROUP_CODE, ApiGroupField.GROUP_NAME))
                .condition(ConditionNode.in(ApiGroupField.GROUP_NO.getFieldName(), groupNoList))
                .build();
        List<ApiGroupIDTO> apiGroupList = apiGroupRepository.selectList(groupQueryParam);
        if (CollUtil.isEmpty(apiGroupList)) {
            return Map.of();
        }
        return CollUtil.toMap(
                apiGroupList,
                null,
                ApiGroupIDTO::getGroupNo
        );
    }
}
