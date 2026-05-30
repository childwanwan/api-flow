package com.apiflow.domain.plugin.service;

import com.apiflow.api.repository.plugin.PluginConfigRepository;
import com.apiflow.api.repository.plugin.idto.PluginConfigIDTO;
import com.apiflow.api.repository.plugin.param.SelectOnePluginConfigParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.FieldCondition;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PluginConfigDomainService {

    private final PluginConfigRepository pluginConfigRepository;

    public void validatePluginCodeNotExists(String pluginCode) {
        SelectOnePluginConfigParam param = SelectOnePluginConfigParam.builder()
                .pluginCode(FieldCondition.of(pluginCode)).build();
        PluginConfigIDTO existing = pluginConfigRepository.selectOne(param);
        if (existing != null && !Boolean.TRUE.equals(existing.getDeleted())) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "插件编码已存在");
        }
    }

    public PluginConfigIDTO requireExistingPlugin(String pluginCode) {
        SelectOnePluginConfigParam param = SelectOnePluginConfigParam.builder()
                .pluginCode(FieldCondition.of(pluginCode)).build();
        PluginConfigIDTO existing = pluginConfigRepository.selectOne(param);
        if (existing == null || Boolean.TRUE.equals(existing.getDeleted())) {
            throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND);
        }
        return existing;
    }
}
