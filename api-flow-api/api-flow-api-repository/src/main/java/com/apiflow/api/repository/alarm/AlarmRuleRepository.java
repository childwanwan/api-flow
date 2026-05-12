package com.apiflow.api.repository.alarm;

import com.apiflow.api.repository.alarm.idto.AlarmRuleIDTO;
import com.apiflow.api.repository.alarm.param.SaveAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.UpdateAlarmRuleParam;

import java.util.List;

public interface AlarmRuleRepository {

    AlarmRuleIDTO save(SaveAlarmRuleParam param);

    AlarmRuleIDTO update(UpdateAlarmRuleParam param);

    AlarmRuleIDTO selectById(Long id);

    List<AlarmRuleIDTO> selectList(SelectAlarmRuleParam param);

    long delete(Long id);
}
