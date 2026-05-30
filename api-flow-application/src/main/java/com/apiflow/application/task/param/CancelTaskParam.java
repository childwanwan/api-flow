package com.apiflow.application.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelTaskParam {
    private String taskNo;
    private String reason;
    private String canceledBy;
}
