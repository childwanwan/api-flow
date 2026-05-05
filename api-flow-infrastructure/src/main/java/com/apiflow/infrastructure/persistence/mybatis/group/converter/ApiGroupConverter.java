package com.apiflow.infrastructure.persistence.mybatis.group.converter;

import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.SaveApiGroupParam;
import com.apiflow.api.repository.group.param.UpdateApiGroupParam;
import com.apiflow.infrastructure.persistence.mybatis.group.entity.ApiGroupPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApiGroupConverter {

    ApiGroupConverter INSTANCE = Mappers.getMapper(ApiGroupConverter.class);

    @Named("saveParamToPO")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    ApiGroupPO saveParamToPO(SaveApiGroupParam param);

    @Named("updateParamToPO")
    ApiGroupPO updateParamToPO(UpdateApiGroupParam param);

    @Named("poToIDTO")
    ApiGroupIDTO poToIDTO(ApiGroupPO po);
}
