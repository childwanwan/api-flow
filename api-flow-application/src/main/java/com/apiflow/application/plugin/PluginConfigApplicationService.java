package com.apiflow.application.plugin;

import com.apiflow.api.repository.plugin.PluginConfigRepository;
import com.apiflow.api.repository.plugin.idto.PluginConfigIDTO;
import com.apiflow.api.repository.plugin.param.SavePluginConfigParam;
import com.apiflow.api.repository.plugin.param.SelectOnePluginConfigParam;
import com.apiflow.api.repository.plugin.param.SelectPluginConfigParam;
import com.apiflow.api.repository.plugin.param.UpdatePluginConfigParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.domain.plugin.PluginChainExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PluginConfigApplicationService {

    private final PluginConfigRepository pluginConfigRepository;
    private final PluginChainExecutor pluginChainExecutor;

    public PluginConfigIDTO createPlugin(String pluginCode, String pluginName, String pluginClass,
                                          String description, String config, Boolean enabled, Integer orderNum) {
        SelectOnePluginConfigParam param = SelectOnePluginConfigParam.builder()
                .pluginCode(FieldCondition.of(pluginCode)).build();
        PluginConfigIDTO existing = pluginConfigRepository.selectOne(param);
        if (existing != null && !Boolean.TRUE.equals(existing.getDeleted())) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "插件编码已存在");
        }

        long now = System.currentTimeMillis();
        SavePluginConfigParam saveParam = SavePluginConfigParam.builder()
                .pluginCode(pluginCode)
                .pluginName(pluginName)
                .pluginClass(pluginClass)
                .description(description)
                .config(config)
                .enabled(enabled != null ? enabled : true)
                .orderNum(orderNum != null ? orderNum : 0)
                .createTimeMs(now)
                .updateTimeMs(now)
                .build();
        return pluginConfigRepository.save(saveParam);
    }

    public PluginConfigIDTO updatePlugin(String pluginCode, String pluginName, String pluginClass,
                                          String description, String config, Boolean enabled, Integer orderNum) {
        SelectOnePluginConfigParam param = SelectOnePluginConfigParam.builder()
                .pluginCode(FieldCondition.of(pluginCode)).build();
        PluginConfigIDTO existing = pluginConfigRepository.selectOne(param);
        if (existing == null || Boolean.TRUE.equals(existing.getDeleted())) {
            throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND);
        }

        UpdatePluginConfigParam updateParam = UpdatePluginConfigParam.builder()
                .id(existing.getId())
                .pluginCode(pluginCode)
                .pluginName(pluginName)
                .pluginClass(pluginClass)
                .description(description)
                .config(config)
                .enabled(enabled)
                .orderNum(orderNum)
                .updateTimeMs(System.currentTimeMillis())
                .build();
        return pluginConfigRepository.update(updateParam);
    }

    public PluginConfigIDTO getPlugin(String pluginCode) {
        SelectOnePluginConfigParam param = SelectOnePluginConfigParam.builder()
                .pluginCode(FieldCondition.of(pluginCode)).build();
        PluginConfigIDTO plugin = pluginConfigRepository.selectOne(param);
        if (plugin == null || Boolean.TRUE.equals(plugin.getDeleted())) {
            throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND);
        }
        return plugin;
    }

    public List<PluginConfigIDTO> listPlugins(String pluginCode, String pluginName) {
        SelectPluginConfigParam.SelectPluginConfigParamBuilder builder = SelectPluginConfigParam.builder();
        if (pluginCode != null) {
            builder.pluginCode(FieldCondition.<String>builder().like(pluginCode).build());
        }
        if (pluginName != null) {
            builder.pluginName(FieldCondition.<String>builder().like(pluginName).build());
        }
        return pluginConfigRepository.selectList(builder.build());
    }

    public void deletePlugin(String pluginCode) {
        SelectOnePluginConfigParam param = SelectOnePluginConfigParam.builder()
                .pluginCode(FieldCondition.of(pluginCode)).build();
        PluginConfigIDTO existing = pluginConfigRepository.selectOne(param);
        if (existing == null || Boolean.TRUE.equals(existing.getDeleted())) {
            throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND);
        }
        pluginChainExecutor.unregisterPlugin(pluginCode);
    }

    public PluginConfigIDTO enablePlugin(String pluginCode) {
        return updatePluginEnabled(pluginCode, true);
    }

    public PluginConfigIDTO disablePlugin(String pluginCode) {
        return updatePluginEnabled(pluginCode, false);
    }

    private PluginConfigIDTO updatePluginEnabled(String pluginCode, boolean enabled) {
        SelectOnePluginConfigParam param = SelectOnePluginConfigParam.builder()
                .pluginCode(FieldCondition.of(pluginCode)).build();
        PluginConfigIDTO existing = pluginConfigRepository.selectOne(param);
        if (existing == null || Boolean.TRUE.equals(existing.getDeleted())) {
            throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND);
        }

        UpdatePluginConfigParam updateParam = UpdatePluginConfigParam.builder()
                .id(existing.getId())
                .enabled(enabled)
                .updateTimeMs(System.currentTimeMillis())
                .build();
        return pluginConfigRepository.update(updateParam);
    }
}
