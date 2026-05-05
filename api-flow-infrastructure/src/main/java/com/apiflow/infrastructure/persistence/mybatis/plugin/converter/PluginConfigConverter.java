package com.apiflow.infrastructure.persistence.mybatis.plugin.converter;

import com.apiflow.api.repository.plugin.idto.PluginConfigIDTO;
import com.apiflow.api.repository.plugin.param.SavePluginConfigParam;
import com.apiflow.api.repository.plugin.param.UpdatePluginConfigParam;
import com.apiflow.infrastructure.persistence.mybatis.plugin.entity.PluginConfigPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PluginConfigConverter {

    PluginConfigConverter INSTANCE = Mappers.getMapper(PluginConfigConverter.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    PluginConfigPO saveParamToPO(SavePluginConfigParam param);

    PluginConfigPO updateParamToPO(UpdatePluginConfigParam param);

    PluginConfigIDTO poToIDTO(PluginConfigPO po);
}
