package com.apiflow.api.repository.alarm.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveAlarmRecordParam {
    private String eventType;
    private String level;
    private String message;
    private String detail;
    private String taskNo;
    private String apiCode;
    private Long createTimeMs;
}
