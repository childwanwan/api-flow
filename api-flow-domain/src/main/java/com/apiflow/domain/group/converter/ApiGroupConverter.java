package com.apiflow.domain.group.converter;

import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.SaveApiGroupParam;
import com.apiflow.api.repository.group.param.UpdateApiGroupParam;
import com.apiflow.domain.group.model.ApiGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(implementationName = "DomainApiGroupConverter")
public interface ApiGroupConverter {

    ApiGroupConverter INSTANCE = Mappers.getMapper(ApiGroupConverter.class);

    ApiGroup toAggregate(ApiGroupIDTO idto);

    SaveApiGroupParam toSaveParam(ApiGroup aggregate);

    UpdateApiGroupParam toUpdateParam(ApiGroup aggregate);
}
