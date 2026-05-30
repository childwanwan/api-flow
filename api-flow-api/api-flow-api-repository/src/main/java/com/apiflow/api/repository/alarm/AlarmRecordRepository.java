package com.apiflow.api.repository.alarm;

import com.apiflow.api.repository.alarm.idto.AlarmRecordIDTO;
import com.apiflow.api.repository.alarm.param.SaveAlarmRecordParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRecordParam;

import java.util.List;

public interface AlarmRecordRepository {

    void save(SaveAlarmRecordParam param);

    List<AlarmRecordIDTO> selectList(SelectAlarmRecordParam param);
}
