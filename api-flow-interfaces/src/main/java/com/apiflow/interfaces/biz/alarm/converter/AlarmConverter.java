package com.apiflow.interfaces.biz.alarm.converter;

import com.apiflow.api.repository.alarm.idto.AlarmRecordIDTO;
import com.apiflow.api.repository.alarm.idto.AlarmRuleIDTO;
import com.apiflow.interfaces.biz.alarm.vo.AlarmRecordVO;
import com.apiflow.interfaces.biz.alarm.vo.AlarmRuleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AlarmConverter {
    AlarmConverter INSTANCE = Mappers.getMapper(AlarmConverter.class);

    AlarmRuleVO toRuleVO(AlarmRuleIDTO idto);
    List<AlarmRuleVO> toRuleVOList(List<AlarmRuleIDTO> idtos);
    AlarmRecordVO toRecordVO(AlarmRecordIDTO idto);
    List<AlarmRecordVO> toRecordVOList(List<AlarmRecordIDTO> idtos);
}
