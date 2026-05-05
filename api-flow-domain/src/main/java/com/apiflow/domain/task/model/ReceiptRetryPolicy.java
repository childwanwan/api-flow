package com.apiflow.domain.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptRetryPolicy {

    private Integer maxRetries;
    private List<Long> retryIntervals;
}
