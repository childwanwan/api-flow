package com.apiflow.domain.task.model;

import com.apiflow.common.enums.CompensateStatus;
import com.apiflow.common.enums.TaskStatus;
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
    private RequestContext requestContext;
    private ExecInfo execInfo;
    private String responseData;
    private Long expireTimeMs;
    private ReceiptConfig receiptConfig;
    private ReceiptInfo receiptInfo;
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
        return TaskStatus.PENDING.getValue().equals(status) || TaskStatus.RUNNING.getValue().equals(status);
    }

    public boolean canRetry() {
        if (!TaskStatus.FAILED.getValue().equals(status)) {
            return false;
        }
        return retryCount == null || maxRetryCount == null || retryCount < maxRetryCount;
    }

    public boolean canExecute() {
        return TaskStatus.PENDING.getValue().equals(status) && Boolean.FALSE.equals(interruptFlag);
    }

    public void cancel(String reason, String cancelBy) {
        this.interruptFlag = true;
        this.cancelReason = reason;
        this.canceledBy = cancelBy;
        this.cancelTimeMs = System.currentTimeMillis();
        this.status = TaskStatus.CANCELED.getValue();
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void prepareRetry(long retryIntervalMs) {
        this.retryCount = (this.retryCount == null ? 0 : this.retryCount) + 1;
        this.status = TaskStatus.PENDING.getValue();
        this.interruptFlag = false;
        this.nextRetryTimeMs = System.currentTimeMillis() + retryIntervalMs * (1L << Math.min(this.retryCount, 30));
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void startExecute() {
        this.status = TaskStatus.RUNNING.getValue();
        this.startExecuteTimeMs = System.currentTimeMillis();
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void complete(String responseData) {
        this.status = TaskStatus.SUCCESS.getValue();
        this.responseData = responseData;
        this.endExecuteTimeMs = System.currentTimeMillis();
        this.executeDurationMs = this.endExecuteTimeMs - (this.startExecuteTimeMs != null ? this.startExecuteTimeMs : this.createTimeMs);
        this.compensateStatus = CompensateStatus.SUCCESS.getValue();
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void fail() {
        this.status = TaskStatus.FAILED.getValue();
        this.endExecuteTimeMs = System.currentTimeMillis();
        this.compensateStatus = CompensateStatus.PENDING.getValue();
        this.updateTimeMs = System.currentTimeMillis();
    }
}
