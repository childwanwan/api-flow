package com.flow.api.repository.task.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskHttpReceiptIDTO {
    private String url;
    private String method;
    private Map<String, String> headers;
    private Object bodyTemplate;
    private TaskReceiptRetryPolicyIDTO retryPolicy;
    private Long timeoutMs;
}
