package com.apiflow.infrastructure.persistence.mybatis.operationlog.converter;

import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.SaveOperationLogParam;
import com.apiflow.infrastructure.persistence.mybatis.operationlog.entity.OperationLogPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OperationLogConverter {

    OperationLogConverter INSTANCE = Mappers.getMapper(OperationLogConverter.class);

    @Mapping(target = "id", ignore = true)
    OperationLogPO saveParamToPO(SaveOperationLogParam param);

    OperationLogIDTO poToIDTO(OperationLogPO po);
}
