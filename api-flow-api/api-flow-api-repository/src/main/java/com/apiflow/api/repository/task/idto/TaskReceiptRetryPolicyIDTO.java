package com.apiflow.api.repository.task.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskReceiptRetryPolicyIDTO {
    private Integer maxRetries;
    private List<Long> retryIntervals;
}
