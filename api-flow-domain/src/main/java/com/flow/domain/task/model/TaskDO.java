package com.flow.domain.task.model;

import com.flow.api.repository.task.idto.TaskExecInfoIDTO;
import com.flow.api.repository.task.idto.TaskReceiptConfigIDTO;
import com.flow.api.repository.task.idto.TaskReceiptInfoIDTO;
import com.flow.api.repository.task.idto.TaskRequestContextIDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDO {

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

    public boolean canCancel() {
        return "PENDING".equals(status) || "RUNNING".equals(status);
    }

    public boolean canRetry() {
        if (!"FAILED".equals(status)) {
            return false;
        }
        return retryCount == null || maxRetryCount == null || retryCount < maxRetryCount;
    }

    public boolean canExecute() {
        return "PENDING".equals(status) && Boolean.FALSE.equals(interruptFlag);
    }

    public void cancel(String reason, String cancelBy) {
        this.interruptFlag = true;
        this.cancelReason = reason;
        this.canceledBy = cancelBy;
        this.cancelTimeMs = System.currentTimeMillis();
        this.status = "CANCELED";
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void prepareRetry() {
        this.retryCount = (this.retryCount == null ? 0 : this.retryCount) + 1;
        this.status = "PENDING";
        this.interruptFlag = false;
        long baseInterval = 1000L;
        this.nextRetryTimeMs = System.currentTimeMillis() + baseInterval * (1L << Math.min(this.retryCount, 30));
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void startExecute() {
        this.status = "RUNNING";
        this.startExecuteTimeMs = System.currentTimeMillis();
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void complete(Object responseData) {
        this.status = "SUCCESS";
        this.responseData = responseData;
        this.endExecuteTimeMs = System.currentTimeMillis();
        this.executeDurationMs = this.endExecuteTimeMs - (this.startExecuteTimeMs != null ? this.startExecuteTimeMs : this.createTimeMs);
        this.compensateStatus = "SUCCESS";
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void fail() {
        this.status = "FAILED";
        this.endExecuteTimeMs = System.currentTimeMillis();
        this.compensateStatus = "PENDING";
        this.updateTimeMs = System.currentTimeMillis();
    }
}
