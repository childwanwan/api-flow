package com.apiflow.infrastructure.persistence.mybatis.alarm.converter;

import com.apiflow.api.repository.alarm.idto.AlarmRuleIDTO;
import com.apiflow.api.repository.alarm.param.SaveAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.UpdateAlarmRuleParam;
import com.apiflow.infrastructure.persistence.mybatis.alarm.entity.AlarmRulePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AlarmRuleConverter {

    AlarmRuleConverter INSTANCE = Mappers.getMapper(AlarmRuleConverter.class);

    @Mapping(target = "id", ignore = true)
    AlarmRulePO saveParamToPO(SaveAlarmRuleParam param);

    AlarmRulePO updateParamToPO(UpdateAlarmRuleParam param);

    AlarmRuleIDTO poToIDTO(AlarmRulePO po);
}
