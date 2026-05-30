package com.apiflow.infrastructure.persistence.mybatis.alarm.converter;

import com.apiflow.api.repository.alarm.idto.AlarmRecordIDTO;
import com.apiflow.api.repository.alarm.param.SaveAlarmRecordParam;
import com.apiflow.infrastructure.persistence.mybatis.alarm.entity.AlarmRecordPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AlarmRecordConverter {

    AlarmRecordConverter INSTANCE = Mappers.getMapper(AlarmRecordConverter.class);

    @Mapping(target = "id", ignore = true)
    AlarmRecordPO saveParamToPO(SaveAlarmRecordParam param);

    AlarmRecordIDTO poToIDTO(AlarmRecordPO po);
}
