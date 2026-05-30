package com.apiflow.application.plugin;

import cn.hutool.core.util.StrUtil;
import com.apiflow.api.repository.plugin.PluginConfigRepository;
import com.apiflow.api.repository.plugin.idto.PluginConfigIDTO;
import com.apiflow.api.repository.plugin.param.SavePluginConfigParam;
import com.apiflow.api.repository.plugin.param.SelectOnePluginConfigParam;
import com.apiflow.api.repository.plugin.param.SelectPluginConfigParam;
import com.apiflow.application.plugin.converter.PluginConfigConverter;
import com.apiflow.application.plugin.param.CreatePluginConfigParam;
import com.apiflow.application.plugin.param.DeletePluginConfigParam;
import com.apiflow.application.plugin.param.DisablePluginConfigParam;
import com.apiflow.application.plugin.param.EnablePluginConfigParam;
import com.apiflow.application.plugin.param.GetPluginConfigParam;
import com.apiflow.application.plugin.param.ListPluginConfigParam;
import com.apiflow.application.plugin.param.UpdatePluginConfigParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.domain.plugin.PluginChainExecutor;
import com.apiflow.domain.plugin.service.PluginConfigDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PluginConfigApplicationService {

    private static final PluginConfigConverter CONVERTER = PluginConfigConverter.INSTANCE;

    private final PluginConfigRepository pluginConfigRepository;
    private final PluginConfigDomainService pluginConfigDomainService;
    private final PluginChainExecutor pluginChainExecutor;
    private final TransactionTemplate transactionTemplate;

    public PluginConfigIDTO createPlugin(CreatePluginConfigParam param) {
        pluginConfigDomainService.validatePluginCodeNotExists(param.getPluginCode());

        SavePluginConfigParam saveParam = CONVERTER.toSaveParam(param);
        transactionTemplate.executeWithoutResult(status -> {
            pluginConfigRepository.save(saveParam);
        });
        SelectOnePluginConfigParam queryParam = SelectOnePluginConfigParam.builder()
                .pluginCode(FieldCondition.of(param.getPluginCode())).build();
        return pluginConfigRepository.selectOne(queryParam);
    }

    public PluginConfigIDTO updatePlugin(UpdatePluginConfigParam param) {
        SelectOnePluginConfigParam queryParam = SelectOnePluginConfigParam.builder()
                .pluginCode(FieldCondition.of(param.getPluginCode())).build();
        PluginConfigIDTO existing = pluginConfigRepository.selectOne(queryParam);
        if (existing == null || Boolean.TRUE.equals(existing.getDeleted())) {
            throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND);
        }

        com.apiflow.api.repository.plugin.param.UpdatePluginConfigParam updateParam = CONVERTER.toUpdateParam(existing.getId(), param);
        transactionTemplate.executeWithoutResult(status -> {
            pluginConfigRepository.update(updateParam);
        });
        return pluginConfigRepository.selectOne(queryParam);
    }

    public PluginConfigIDTO getPlugin(GetPluginConfigParam param) {
        return pluginConfigDomainService.requireExistingPlugin(param.getPluginCode());
    }

    public List<PluginConfigIDTO> listPlugins(ListPluginConfigParam param) {
        SelectPluginConfigParam.SelectPluginConfigParamBuilder builder = SelectPluginConfigParam.builder();
        if (StrUtil.isNotEmpty(param.getPluginCode())) {
            builder.pluginCode(FieldCondition.<String>builder().like(param.getPluginCode()).build());
        }
        if (StrUtil.isNotEmpty(param.getPluginName())) {
            builder.pluginName(FieldCondition.<String>builder().like(param.getPluginName()).build());
        }
        return pluginConfigRepository.selectList(builder.build());
    }

    public void deletePlugin(DeletePluginConfigParam param) {
        pluginConfigDomainService.requireExistingPlugin(param.getPluginCode());
        transactionTemplate.executeWithoutResult(status -> {
            pluginChainExecutor.unregisterPlugin(param.getPluginCode());
        });
    }

    public PluginConfigIDTO enablePlugin(EnablePluginConfigParam param) {
        return updatePluginEnabled(param.getPluginCode(), true);
    }

    public PluginConfigIDTO disablePlugin(DisablePluginConfigParam param) {
        return updatePluginEnabled(param.getPluginCode(), false);
    }

    private PluginConfigIDTO updatePluginEnabled(String pluginCode, boolean enabled) {
        PluginConfigIDTO existing = pluginConfigDomainService.requireExistingPlugin(pluginCode);

        com.apiflow.api.repository.plugin.param.UpdatePluginConfigParam updateParam =
                CONVERTER.toEnabledUpdateParam(existing.getId(), enabled);
        transactionTemplate.executeWithoutResult(status -> {
            pluginConfigRepository.update(updateParam);
        });
        SelectOnePluginConfigParam queryParam = SelectOnePluginConfigParam.builder()
                .pluginCode(FieldCondition.of(pluginCode)).build();
        return pluginConfigRepository.selectOne(queryParam);
    }
}
