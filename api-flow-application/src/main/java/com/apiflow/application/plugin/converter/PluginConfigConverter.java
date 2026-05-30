package com.apiflow.application.plugin.converter;

import com.apiflow.api.repository.plugin.param.SavePluginConfigParam;
import com.apiflow.api.repository.plugin.param.UpdatePluginConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PluginConfigConverter {

    PluginConfigConverter INSTANCE = Mappers.getMapper(PluginConfigConverter.class);

    @Mapping(target = "enabled", qualifiedByName = "defaultEnabled")
    @Mapping(target = "orderNum", qualifiedByName = "defaultOrderNum")
    @Mapping(target = "createTimeMs", expression = "java(System.currentTimeMillis())")
    @Mapping(target = "updateTimeMs", expression = "java(System.currentTimeMillis())")
    SavePluginConfigParam toSaveParam(com.apiflow.application.plugin.param.CreatePluginConfigParam param);

    @Mapping(target = "updateTimeMs", expression = "java(System.currentTimeMillis())")
    UpdatePluginConfigParam toUpdateParam(Long id, com.apiflow.application.plugin.param.UpdatePluginConfigParam param);

    @Mapping(target = "pluginCode", ignore = true)
    @Mapping(target = "pluginName", ignore = true)
    @Mapping(target = "pluginClass", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "config", ignore = true)
    @Mapping(target = "orderNum", ignore = true)
    @Mapping(target = "updateTimeMs", expression = "java(System.currentTimeMillis())")
    UpdatePluginConfigParam toEnabledUpdateParam(Long id, Boolean enabled);

    @Named("defaultEnabled")
    default Boolean defaultEnabled(Boolean enabled) {
        return enabled != null ? enabled : true;
    }

    @Named("defaultOrderNum")
    default Integer defaultOrderNum(Integer orderNum) {
        return orderNum != null ? orderNum : 0;
    }
}
