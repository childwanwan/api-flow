package com.apiflow.api.repository.alarm;

import com.apiflow.api.repository.alarm.idto.AlarmRuleIDTO;
import com.apiflow.api.repository.alarm.param.SaveAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.SelectOneAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.UpdateAlarmRuleParam;

import java.util.List;

public interface AlarmRuleRepository {

    void save(SaveAlarmRuleParam param);

    void update(UpdateAlarmRuleParam param);

    AlarmRuleIDTO selectById(Long id);

    AlarmRuleIDTO selectOne(SelectOneAlarmRuleParam param);

    List<AlarmRuleIDTO> selectList(SelectAlarmRuleParam param);

    void deleteList(List<Long> idList);
}
