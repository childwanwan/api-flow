package com.apiflow.api.repository.configlog.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveConfigChangeLogParam {
    private String apiCode;
    private String changeType;
    private String beforeConfig;
    private String afterConfig;
    private String operator;
    private Long createTimeMs;
}
