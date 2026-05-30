package com.apiflow.interfaces.biz.alarm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmRecordVO {
    private Long id;
    private String eventType;
    private String level;
    private String message;
    private String detail;
    private String taskNo;
    private String apiCode;
    private Long createTimeMs;
}
