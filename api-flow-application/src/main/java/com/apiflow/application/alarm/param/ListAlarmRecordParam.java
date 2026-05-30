package com.apiflow.application.alarm.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListAlarmRecordParam {
    private String eventType;
    private String level;
    private Long startTimeMs;
    private Long endTimeMs;
    private Integer limit;
}
