package com.apiflow.application.alarm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.apiflow.api.repository.alarm.AlarmRecordRepository;
import com.apiflow.api.repository.alarm.AlarmRuleRepository;
import com.apiflow.api.repository.alarm.idto.AlarmRecordIDTO;
import com.apiflow.api.repository.alarm.idto.AlarmRuleIDTO;
import com.apiflow.api.repository.alarm.param.SaveAlarmRecordParam;
import com.apiflow.api.repository.alarm.param.SaveAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRecordParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRuleParam;
import com.apiflow.application.alarm.param.CreateAlarmRuleParam;
import com.apiflow.application.alarm.param.DeleteAlarmRuleParam;
import com.apiflow.application.alarm.param.ListAlarmRecordParam;
import com.apiflow.application.alarm.param.ListAlarmRuleParam;
import com.apiflow.application.alarm.param.UpdateAlarmRuleParam;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.application.alarm.converter.AlarmConverter;
import com.apiflow.domain.alarm.AlarmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmApplicationService {

    private final AlarmRuleRepository alarmRuleRepository;
    private final AlarmRecordRepository alarmRecordRepository;
    private final TransactionTemplate transactionTemplate;

    public AlarmRuleIDTO createRule(CreateAlarmRuleParam param) {
        SaveAlarmRuleParam saveParam = AlarmConverter.INSTANCE.toSaveRuleParam(
                param.getRuleName(), param.getAlarmType(), param.getTriggerCondition(),
                param.getLevel(), param.getEnabled());
        transactionTemplate.executeWithoutResult(status -> {
            alarmRuleRepository.save(saveParam);
        });
        SelectAlarmRuleParam queryParam = SelectAlarmRuleParam.builder()
                .limit(1)
                .build();
        List<AlarmRuleIDTO> list = alarmRuleRepository.selectList(queryParam);
        return CollUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    public AlarmRuleIDTO updateRule(UpdateAlarmRuleParam param) {
        com.apiflow.api.repository.alarm.param.UpdateAlarmRuleParam updateParam =
                AlarmConverter.INSTANCE.toUpdateRuleParam(
                        param.getId(), param.getRuleName(), param.getAlarmType(),
                        param.getTriggerCondition(), param.getLevel(), param.getEnabled());
        transactionTemplate.executeWithoutResult(status -> {
            alarmRuleRepository.update(updateParam);
        });
        return alarmRuleRepository.selectById(param.getId());
    }

    public List<AlarmRuleIDTO> listRules(ListAlarmRuleParam param) {
        SelectAlarmRuleParam.SelectAlarmRuleParamBuilder builder = SelectAlarmRuleParam.builder();
        if (StrUtil.isNotEmpty(param.getAlarmType())) {
            builder.alarmType(FieldCondition.of(param.getAlarmType()));
        }
        if (param.getEnabled() != null) {
            builder.enabled(FieldCondition.of(param.getEnabled()));
        }
        builder.limit(100);
        return alarmRuleRepository.selectList(builder.build());
    }

    public void deleteRule(DeleteAlarmRuleParam param) {
        transactionTemplate.executeWithoutResult(status -> {
            alarmRuleRepository.deleteList(List.of(param.getId()));
        });
    }

    public List<AlarmRecordIDTO> listRecords(ListAlarmRecordParam param) {
        SelectAlarmRecordParam.SelectAlarmRecordParamBuilder builder = SelectAlarmRecordParam.builder();
        if (StrUtil.isNotEmpty(param.getEventType())) {
            builder.eventType(FieldCondition.of(param.getEventType()));
        }
        if (StrUtil.isNotEmpty(param.getLevel())) {
            builder.level(FieldCondition.of(param.getLevel()));
        }
        if (param.getStartTimeMs() != null && param.getEndTimeMs() != null) {
            builder.createTimeMs(FieldCondition.<Long>builder().betweenStart(param.getStartTimeMs()).betweenEnd(param.getEndTimeMs()).build());
        }
        builder.limit(param.getLimit() != null ? param.getLimit() : 100);
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
            SaveAlarmRecordParam saveParam = AlarmConverter.INSTANCE.toSaveRecordParam(
                    event.getEventType(), event.getLevel(), event.getMessage(),
                    event.getDetail() != null ? event.getDetail().toString() : null,
                    taskNo, apiCode, event.getEventTime());
            transactionTemplate.executeWithoutResult(status -> {
                alarmRecordRepository.save(saveParam);
            });
        } catch (Exception e) {
            log.error("Failed to record alarm event", e);
        }
    }
}
