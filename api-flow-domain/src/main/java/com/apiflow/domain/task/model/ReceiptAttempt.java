package com.apiflow.domain.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptAttempt {

    private Integer attempt;
    private Boolean success;
    private Long timestampMs;
    private Integer responseCode;
    private String responseBody;
    private Integer partition;
    private Long offset;
    private String errorMessage;
}
