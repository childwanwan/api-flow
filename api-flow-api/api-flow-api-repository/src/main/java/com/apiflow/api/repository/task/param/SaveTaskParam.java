package com.apiflow.api.repository.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveTaskParam {
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
    private SaveTaskRequestContextParam requestContext;
    private SaveTaskExecInfoParam execInfo;
    private Object responseData;
    private Long expireTimeMs;
    private SaveTaskReceiptConfigParam receiptConfig;
    private SaveTaskReceiptInfoParam receiptInfo;
    private Integer retryCount;
    private Integer maxRetryCount;
    private Long nextRetryTimeMs;
    private Long startExecuteTimeMs;
    private Long endExecuteTimeMs;
    private Long executeDurationMs;
    private Long cancelTimeMs;
    private String cancelReason;
    private String canceledBy;
}
