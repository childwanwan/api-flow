package com.apiflow.interfaces.biz.config.request;

import com.apiflow.interfaces.util.ValidationHelper;
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
public class ApiConfigReceiptConfigRequest {

    private List<String> receiptTypes;
    private HttpReceiptRequest http;
    private MqReceiptRequest mq;

    public void validate() {
        if (http != null) {
            http.validate();
        }
        if (mq != null) {
            mq.validate();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HttpReceiptRequest {
        private String url;
        private String method;
        private Map<String, String> headers;
        private Object bodyTemplate;
        private RetryPolicyRequest retryPolicy;
        private Long timeoutMs;

        public void validate() {
            ValidationHelper.validateSize(url, 2048, "http.url");
            if (method != null) {
                ValidationHelper.validatePattern(method, "^(GET|POST|PUT)$", "http.method");
            }
            if (retryPolicy != null) {
                retryPolicy.validate();
            }
            if (timeoutMs != null) {
                ValidationHelper.validateRange(timeoutMs, 100L, 300000L, "http.timeoutMs");
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RetryPolicyRequest {
        private Integer maxRetries;

        public void validate() {
            if (maxRetries != null) {
                ValidationHelper.validateRange(maxRetries.longValue(), 0L, 64L, "http.retryPolicy.maxRetries");
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MqReceiptRequest {
        private String topic;
        private Map<String, String> headers;
        private Object bodyTemplate;

        public void validate() {
            ValidationHelper.validateSize(topic, 256, "mq.topic");
        }
    }
}
