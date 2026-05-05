package com.apiflow.interfaces.biz.task.request;

import com.apiflow.interfaces.util.ValidationHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRetryRequest {

    private String taskNo;
    private String retryOperator;

    public void validate() {
        ValidationHelper.validateNotBlank(taskNo, "taskNo");
        ValidationHelper.validateSize(taskNo, 64, "taskNo");
        ValidationHelper.validatePattern(taskNo, "^[a-zA-Z0-9_-]+$", "taskNo");
        ValidationHelper.validateSize(retryOperator, 64, "retryOperator");
    }
}
