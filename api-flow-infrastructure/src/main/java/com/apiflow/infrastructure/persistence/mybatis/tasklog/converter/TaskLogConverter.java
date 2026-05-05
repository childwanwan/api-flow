package com.apiflow.infrastructure.persistence.mybatis.tasklog.converter;

import com.apiflow.api.repository.tasklog.idto.TaskLogIDTO;
import com.apiflow.api.repository.tasklog.param.SaveTaskLogParam;
import com.apiflow.infrastructure.persistence.mybatis.tasklog.entity.TaskLogPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskLogConverter {

    TaskLogConverter INSTANCE = Mappers.getMapper(TaskLogConverter.class);

    @Mapping(target = "id", ignore = true)
    TaskLogPO saveParamToPO(SaveTaskLogParam param);

    TaskLogIDTO poToIDTO(TaskLogPO po);
}
