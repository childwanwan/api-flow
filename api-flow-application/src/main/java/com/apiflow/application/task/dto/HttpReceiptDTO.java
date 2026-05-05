package com.apiflow.application.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpReceiptDTO {

    private String url;
    private String method;
    private Map<String, String> headers;
    private Object bodyTemplate;
    private ReceiptRetryPolicyDTO retryPolicy;
    private Long timeoutMs;
}
