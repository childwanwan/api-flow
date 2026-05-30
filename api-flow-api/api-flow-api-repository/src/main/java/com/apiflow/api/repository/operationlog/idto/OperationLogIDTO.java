package com.apiflow.api.repository.operationlog.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogIDTO {
    private Long id;
    private String bizCode;
    private String logType;
    private String logData;
    private String operator;
    private String showDetail;
    private Long operateTimeMs;
    private Long createTimeMs;
}
