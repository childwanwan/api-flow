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
public class TaskCancelRequest {

    private String taskNo;
    private String cancelReason;
    private String canceledBy;

    public void validate() {
        ValidationHelper.validateNotBlank(taskNo, "taskNo");
        ValidationHelper.validateSize(taskNo, 64, "taskNo");
        ValidationHelper.validatePattern(taskNo, "^[a-zA-Z0-9_-]+$", "taskNo");
        ValidationHelper.validateNotBlank(canceledBy, "canceledBy");
        ValidationHelper.validateSize(canceledBy, 64, "canceledBy");
        ValidationHelper.validateSize(cancelReason, 256, "cancelReason");
    }
}
