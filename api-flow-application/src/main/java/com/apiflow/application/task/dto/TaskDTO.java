package com.apiflow.application.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;
    private String taskNo;
    private String source;
    private String groupNo;
    private String apiCode;
    private String apiName;
    private String actionType;
    private String status;
    private Boolean interruptFlag;
    private String compensateStatus;
    private Integer compensateRetryCount;
    private Long compensateNextRetryTimeMs;
    private Integer priority;
    private RequestContextDTO requestContext;
    private ExecInfoDTO execInfo;
    private Object responseData;
    private Long expireTimeMs;
    private ReceiptConfigDTO receiptConfig;
    private ReceiptInfoDTO receiptInfo;
    private Integer retryCount;
    private Integer maxRetryCount;
    private Long nextRetryTimeMs;
    private Long createTimeMs;
    private Long startExecuteTimeMs;
    private Long endExecuteTimeMs;
    private Long executeDurationMs;
    private Long cancelTimeMs;
    private String cancelReason;
    private String canceledBy;
    private Long updateTimeMs;
}
