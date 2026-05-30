package com.apiflow.application.alarm.converter;

import com.apiflow.api.repository.alarm.param.SaveAlarmRecordParam;
import com.apiflow.api.repository.alarm.param.SaveAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.UpdateAlarmRuleParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AlarmConverter {

    AlarmConverter INSTANCE = Mappers.getMapper(AlarmConverter.class);

    @Mapping(target = "level", qualifiedByName = "defaultLevel")
    @Mapping(target = "enabled", qualifiedByName = "defaultEnabled")
    @Mapping(target = "createTimeMs", expression = "java(System.currentTimeMillis())")
    @Mapping(target = "updateTimeMs", expression = "java(System.currentTimeMillis())")
    SaveAlarmRuleParam toSaveRuleParam(String ruleName, String alarmType, String triggerCondition,
                                       String level, Boolean enabled);

    @Mapping(target = "updateTimeMs", expression = "java(System.currentTimeMillis())")
    UpdateAlarmRuleParam toUpdateRuleParam(Long id, String ruleName, String alarmType,
                                           String triggerCondition, String level, Boolean enabled);

    @Mapping(target = "createTimeMs", expression = "java(eventTime != null ? eventTime : System.currentTimeMillis())")
    SaveAlarmRecordParam toSaveRecordParam(String eventType, String level, String message,
                                            String detail, String taskNo, String apiCode, Long eventTime);

    @Named("defaultLevel")
    default String defaultLevel(String level) {
        return level != null ? level : "WARNING";
    }

    @Named("defaultEnabled")
    default Boolean defaultEnabled(Boolean enabled) {
        return enabled != null ? enabled : true;
    }
}
