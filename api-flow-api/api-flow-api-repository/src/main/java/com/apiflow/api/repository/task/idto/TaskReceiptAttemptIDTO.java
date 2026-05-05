package com.apiflow.api.repository.task.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskReceiptAttemptIDTO {
    private Integer attempt;
    private Boolean success;
    private Long timestampMs;
    private Integer responseCode;
    private String responseBody;
    private Integer partition;
    private Long offset;
    private String errorMessage;
}
