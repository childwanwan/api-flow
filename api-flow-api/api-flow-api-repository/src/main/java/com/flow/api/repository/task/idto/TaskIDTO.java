package com.flow.api.repository.task.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskIDTO {
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
    private TaskRequestContextIDTO requestContext;
    private TaskExecInfoIDTO execInfo;
    private Object responseData;
    private Long expireTimeMs;
    private TaskReceiptConfigIDTO receiptConfig;
    private TaskReceiptInfoIDTO receiptInfo;
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
    private Boolean deleted;
    private Integer version;
}
