package com.apiflow.api.repository.operationlog.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveOperationLogParam {
    private String bizCode;
    private String logType;
    private String logData;
    private String operator;
    private Long operateTimeMs;
    private Long createTimeMs;
}
