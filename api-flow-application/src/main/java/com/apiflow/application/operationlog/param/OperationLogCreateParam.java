package com.apiflow.application.operationlog.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogCreateParam {

    private String bizCode;
    private String logType;
    private String logData;
    private String operator;
    private Long operateTimeMs;
    private Long createTimeMs;

}
