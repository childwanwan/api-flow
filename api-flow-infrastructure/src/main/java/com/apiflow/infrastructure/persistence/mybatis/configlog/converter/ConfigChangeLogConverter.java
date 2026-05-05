package com.apiflow.infrastructure.persistence.mybatis.configlog.converter;

import com.apiflow.api.repository.configlog.idto.ConfigChangeLogIDTO;
import com.apiflow.api.repository.configlog.param.SaveConfigChangeLogParam;
import com.apiflow.infrastructure.persistence.mybatis.configlog.entity.ConfigChangeLogPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConfigChangeLogConverter {

    ConfigChangeLogConverter INSTANCE = Mappers.getMapper(ConfigChangeLogConverter.class);

    @Mapping(target = "id", ignore = true)
    ConfigChangeLogPO saveParamToPO(SaveConfigChangeLogParam param);

    ConfigChangeLogIDTO poToIDTO(ConfigChangeLogPO po);
}
