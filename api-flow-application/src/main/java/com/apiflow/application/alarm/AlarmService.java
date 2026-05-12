package com.apiflow.application.alarm;

import com.apiflow.api.repository.alarm.AlarmRecordRepository;
import com.apiflow.api.repository.alarm.AlarmRuleRepository;
import com.apiflow.api.repository.alarm.idto.AlarmRecordIDTO;
import com.apiflow.api.repository.alarm.idto.AlarmRuleIDTO;
import com.apiflow.api.repository.alarm.param.SaveAlarmRecordParam;
import com.apiflow.api.repository.alarm.param.SaveAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRecordParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.UpdateAlarmRuleParam;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.domain.alarm.AlarmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRuleRepository alarmRuleRepository;
    private final AlarmRecordRepository alarmRecordRepository;

    public AlarmRuleIDTO createRule(String ruleName, String alarmType, String triggerCondition, String level, Boolean enabled) {
        long now = System.currentTimeMillis();
        SaveAlarmRuleParam param = SaveAlarmRuleParam.builder()
                .ruleName(ruleName)
                .alarmType(alarmType)
                .triggerCondition(triggerCondition)
                .level(level != null ? level : "WARNING")
                .enabled(enabled != null ? enabled : true)
                .createTimeMs(now)
                .updateTimeMs(now)
                .build();
        return alarmRuleRepository.save(param);
    }

    public AlarmRuleIDTO updateRule(Long id, String ruleName, String alarmType, String triggerCondition, String level, Boolean enabled) {
        UpdateAlarmRuleParam param = UpdateAlarmRuleParam.builder()
                .id(id)
                .ruleName(ruleName)
                .alarmType(alarmType)
                .triggerCondition(triggerCondition)
                .level(level)
                .enabled(enabled)
                .updateTimeMs(System.currentTimeMillis())
                .build();
        return alarmRuleRepository.update(param);
    }

    public List<AlarmRuleIDTO> listRules(String alarmType, Boolean enabled) {
        SelectAlarmRuleParam.SelectAlarmRuleParamBuilder builder = SelectAlarmRuleParam.builder();
        if (alarmType != null && !alarmType.isEmpty()) {
            builder.alarmType(FieldCondition.of(alarmType));
        }
        if (enabled != null) {
            builder.enabled(FieldCondition.of(enabled));
        }
        builder.limit(100);
        return alarmRuleRepository.selectList(builder.build());
    }

    public boolean deleteRule(Long id) {
        return alarmRuleRepository.delete(id) > 0;
    }

    public List<AlarmRecordIDTO> listRecords(String eventType, String level, Long startTimeMs, Long endTimeMs, Integer limit) {
        SelectAlarmRecordParam.SelectAlarmRecordParamBuilder builder = SelectAlarmRecordParam.builder();
        if (eventType != null && !eventType.isEmpty()) {
            builder.eventType(FieldCondition.of(eventType));
        }
        if (level != null && !level.isEmpty()) {
            builder.level(FieldCondition.of(level));
        }
        if (startTimeMs != null && endTimeMs != null) {
            builder.createTimeMs(FieldCondition.<Long>builder().betweenStart(startTimeMs).betweenEnd(endTimeMs).build());
        }
        builder.limit(limit != null ? limit : 100);
        return alarmRecordRepository.selectList(builder.build());
    }

    public void recordAlarmEvent(AlarmEvent event) {
        try {
            String taskNo = null;
            String apiCode = null;
            if (event.getDetail() instanceof java.util.Map) {
                java.util.Map<?, ?> detailMap = (java.util.Map<?, ?>) event.getDetail();
                taskNo = detailMap.get("taskNo") != null ? detailMap.get("taskNo").toString() : null;
                apiCode = detailMap.get("apiCode") != null ? detailMap.get("apiCode").toString() : null;
            }
            SaveAlarmRecordParam param = SaveAlarmRecordParam.builder()
                    .eventType(event.getEventType())
                    .level(event.getLevel())
                    .message(event.getMessage())
                    .detail(event.getDetail() != null ? event.getDetail().toString() : null)
                    .taskNo(taskNo)
                    .apiCode(apiCode)
                    .createTimeMs(event.getEventTime() != null ? event.getEventTime() : System.currentTimeMillis())
                    .build();
            alarmRecordRepository.save(param);
        } catch (Exception e) {
            log.error("Failed to record alarm event", e);
        }
    }
}
