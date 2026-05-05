package com.flow.api.repository.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskReceiptAttemptParam {
    private Integer attempt;
    private Boolean success;
    private Long timestampMs;
    private String responseCode;
    private String responseBody;
    private String partition;
    private String offset;
    private String errorMessage;
}
