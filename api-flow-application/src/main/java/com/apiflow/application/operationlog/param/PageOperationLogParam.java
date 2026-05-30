package com.apiflow.application.operationlog.param;

import com.apiflow.common.dto.BasePageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageOperationLogParam extends BasePageParam {

    private String bizCode;

    private String logType;

    private String operator;

    private Long operateTimeMsStart;

    private Long operateTimeMsEnd;
}
