package com.apiflow.interfaces.biz.plugin.converter;

import com.apiflow.api.repository.plugin.idto.PluginConfigIDTO;
import com.apiflow.interfaces.biz.plugin.vo.PluginConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PluginConfigConverter {
    PluginConfigConverter INSTANCE = Mappers.getMapper(PluginConfigConverter.class);

    PluginConfigVO toVO(PluginConfigIDTO idto);
    List<PluginConfigVO> toVOList(List<PluginConfigIDTO> idtos);
}
