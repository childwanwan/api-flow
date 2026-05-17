package com.apiflow.interfaces.biz.config.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigReceiptConfigVO {

    private List<String> receiptTypes;
    private HttpReceiptVO http;
    private MqReceiptVO mq;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HttpReceiptVO {
        private String url;
        private String method;
        private Map<String, String> headers;
        private Object bodyTemplate;
        private RetryPolicyVO retryPolicy;
        private Long timeoutMs;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RetryPolicyVO {
        private Integer maxRetries;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MqReceiptVO {
        private String topic;
        private Map<String, String> headers;
        private Object bodyTemplate;
    }
}
