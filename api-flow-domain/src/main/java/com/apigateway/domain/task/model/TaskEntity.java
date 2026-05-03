package com.apigateway.domain.task.model;

import com.apigateway.common.constant.SystemConstant;
import com.apigateway.domain.task.enums.ActionType;
import com.apigateway.domain.task.enums.CompensateStatus;
import com.apigateway.domain.task.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    private Long id;
    private String taskNo;
    private String source;
    private String groupNo;
    private String apiCode;
    private String apiName;
    private ActionType actionType;
    private TaskStatus status;
    private Boolean interruptFlag;
    private CompensateStatus compensateStatus;
    private Integer compensateRetryCount;
    private Long compensateNextRetryTimeMs;
    private Integer priority;
    private RequestContext requestContext;
    private ExecInfo execInfo;
    private Object responseData;
    private Long expireTimeMs;
    private String receiptConfig;
    private String receiptInfo;
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

    public void startExecute() {
        this.status = TaskStatus.RUNNING;
        this.startExecuteTimeMs = System.currentTimeMillis();
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void complete(Object responseData) {
        this.status = TaskStatus.SUCCESS;
        this.responseData = responseData;
        this.endExecuteTimeMs = System.currentTimeMillis();
        this.executeDurationMs = this.endExecuteTimeMs - (this.startExecuteTimeMs == null ? this.createTimeMs : this.startExecuteTimeMs);
        if (this.execInfo != null) {
            this.execInfo.setTotalCostTimeMs(this.executeDurationMs);
        }
        this.compensateStatus = CompensateStatus.SUCCESS;
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void fail(String errorMessage) {
        this.status = TaskStatus.FAILED;
        if (this.execInfo != null && !this.execInfo.getSteps().isEmpty()) {
            PluginStep lastStep = this.execInfo.getSteps().get(this.execInfo.getSteps().size() - 1);
            lastStep.setErrorMessage(errorMessage);
            lastStep.setStatus("FAILED");
        }
        this.endExecuteTimeMs = System.currentTimeMillis();
        this.compensateStatus = CompensateStatus.PENDING;
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void cancel(String reason, String cancelBy) {
        this.interruptFlag = true;
        this.cancelReason = reason;
        this.canceledBy = cancelBy;
        this.cancelTimeMs = System.currentTimeMillis();
        this.status = TaskStatus.CANCELED;
        this.updateTimeMs = System.currentTimeMillis();
    }

    public void prepareRetry() {
        this.retryCount = (this.retryCount == null ? 0 : this.retryCount) + 1;
        this.status = TaskStatus.PENDING;
        this.interruptFlag = false;
        if (this.execInfo != null) {
            this.execInfo.setRetryCount(this.retryCount);
        }
        long baseInterval = this.retryIntervalMs == null ? SystemConstant.DEFAULT_RETRY_INTERVAL_MS : this.retryIntervalMs;
        this.nextRetryTimeMs = System.currentTimeMillis() + baseInterval * (1L << Math.min(this.retryCount, 30));
        this.updateTimeMs = System.currentTimeMillis();
    }

    private Long retryIntervalMs;

    public boolean canExecute() {
        return this.status != null && this.status.canExecute() && Boolean.FALSE.equals(this.interruptFlag);
    }

    public boolean canCancel() {
        return this.status != null && this.status.canCancel();
    }

    public boolean canRetry() {
        return this.status != null && this.status.canRetry()
                && (this.retryCount == null || this.maxRetryCount == null || this.retryCount < this.maxRetryCount);
    }

}
