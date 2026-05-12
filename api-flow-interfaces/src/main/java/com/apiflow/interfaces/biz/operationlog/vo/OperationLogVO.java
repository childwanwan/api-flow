package com.apiflow.interfaces.biz.operationlog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogVO {

    private Long id;

    private String bizCode;

    private String logType;

    private String operator;

    private String showDetail;

    private Long operateTimeMs;

    private Long createTimeMs;
}
