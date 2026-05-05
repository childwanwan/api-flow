package com.apiflow.api.repository.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveTaskHttpReceiptParam {
    private String url;
    private String method;
    private Map<String, String> headers;
    private Object bodyTemplate;
    private SaveTaskReceiptRetryPolicyParam retryPolicy;
    private Long timeoutMs;
}
