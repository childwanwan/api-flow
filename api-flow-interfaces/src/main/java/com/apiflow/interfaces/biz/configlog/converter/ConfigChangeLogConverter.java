package com.apiflow.interfaces.biz.configlog.converter;

import com.apiflow.api.repository.configlog.idto.ConfigChangeLogIDTO;
import com.apiflow.interfaces.biz.configlog.vo.ConfigChangeLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ConfigChangeLogConverter {
    ConfigChangeLogConverter INSTANCE = Mappers.getMapper(ConfigChangeLogConverter.class);

    ConfigChangeLogVO toVO(ConfigChangeLogIDTO idto);
    List<ConfigChangeLogVO> toVOList(List<ConfigChangeLogIDTO> idtos);
}
