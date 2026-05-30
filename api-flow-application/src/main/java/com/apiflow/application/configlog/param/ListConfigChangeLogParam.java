package com.apiflow.application.configlog.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListConfigChangeLogParam {
    private String apiCode;
    private String changeType;
    private Long startTimeMs;
    private Long endTimeMs;
    private Integer limit;
}
