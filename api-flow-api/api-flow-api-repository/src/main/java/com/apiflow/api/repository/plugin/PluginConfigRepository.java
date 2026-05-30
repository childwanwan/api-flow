package com.apiflow.api.repository.plugin;

import com.apiflow.api.repository.plugin.idto.PluginConfigIDTO;
import com.apiflow.api.repository.plugin.param.SavePluginConfigParam;
import com.apiflow.api.repository.plugin.param.SelectOnePluginConfigParam;
import com.apiflow.api.repository.plugin.param.SelectPluginConfigParam;
import com.apiflow.api.repository.plugin.param.UpdatePluginConfigParam;

import java.util.List;

public interface PluginConfigRepository {

    PluginConfigIDTO findByPluginCode(String pluginCode);

    void save(SavePluginConfigParam param);

    void update(UpdatePluginConfigParam param);

    PluginConfigIDTO selectOne(SelectOnePluginConfigParam param);

    List<PluginConfigIDTO> selectList(SelectPluginConfigParam param);
}
