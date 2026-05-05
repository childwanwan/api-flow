package com.apiflow.domain.receipt;

import com.apiflow.api.mq.MessageProducer;
import com.apiflow.domain.task.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReceiptService {

    private final MessageProducer messageProducer;
    private final RestTemplate restTemplate;

    public void sendReceipt(TaskDO task, ReceiptConfig receiptConfig) {
        if (receiptConfig == null || receiptConfig.getReceiptTypes() == null) {
            return;
        }
        for (String receiptType : receiptConfig.getReceiptTypes()) {
            try {
                switch (receiptType) {
                    case "HTTP" -> sendHttpReceipt(task, receiptConfig.getHttp());
                    case "MQ" -> sendMqReceipt(task, receiptConfig.getMq());
                    default -> log.warn("Unknown receipt type: {}", receiptType);
                }
            } catch (Exception e) {
                log.error("Failed to send receipt for task: {}, type: {}", task.getTaskNo(), receiptType, e);
            }
        }
    }

    public void sendHttpReceipt(TaskDO task, HttpReceipt httpReceipt) {
        if (httpReceipt == null || httpReceipt.getUrl() == null) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            int maxRetries = httpReceipt.getRetryPolicy() != null && httpReceipt.getRetryPolicy().getMaxRetries() != null
                    ? httpReceipt.getRetryPolicy().getMaxRetries() : 64;
            long baseInterval = 1000;

            for (int attempt = 0; attempt <= maxRetries; attempt++) {
                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    if (httpReceipt.getHeaders() != null) {
                        httpReceipt.getHeaders().forEach(headers::add);
                    }
                    headers.add("X-Request-Id", task.getTaskNo() + "-receipt-" + attempt);

                    Map<String, Object> body = buildReceiptBody(task);
                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

                    long timeoutMs = httpReceipt.getTimeoutMs() != null ? httpReceipt.getTimeoutMs() : 30000;
                    ResponseEntity<String> response = restTemplate.exchange(
                            httpReceipt.getUrl(),
                            HttpMethod.valueOf(httpReceipt.getMethod() != null ? httpReceipt.getMethod() : "POST"),
                            entity,
                            String.class
                    );

                    if (response.getStatusCode().is2xxSuccessful()) {
                        log.info("HTTP receipt sent successfully for task: {}, attempt: {}", task.getTaskNo(), attempt);
                        return;
                    }
                } catch (Exception e) {
                    log.warn("HTTP receipt attempt {} failed for task: {}", attempt, task.getTaskNo(), e);
                }

                if (attempt < maxRetries) {
                    long delay = baseInterval * (1L << Math.min(attempt, 30));
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            log.error("HTTP receipt failed after {} retries for task: {}", maxRetries, task.getTaskNo());
        });
    }

    public void sendMqReceipt(TaskDO task, MqReceipt mqReceipt) {
        if (mqReceipt == null || mqReceipt.getTopic() == null) {
            return;
        }
        try {
            Map<String, String> headers = new HashMap<>();
            if (mqReceipt.getHeaders() != null) {
                headers.putAll(mqReceipt.getHeaders());
            }
            headers.put("taskNo", task.getTaskNo());
            headers.put("apiCode", task.getApiCode());

            messageProducer.send(mqReceipt.getTopic(), buildReceiptBody(task), headers);
            log.info("MQ receipt sent successfully for task: {}", task.getTaskNo());
        } catch (Exception e) {
            log.error("MQ receipt failed for task: {}", task.getTaskNo(), e);
        }
    }

    private Map<String, Object> buildReceiptBody(TaskDO task) {
        Map<String, Object> body = new HashMap<>();
        body.put("taskNo", task.getTaskNo());
        body.put("apiCode", task.getApiCode());
        body.put("apiName", task.getApiName());
        body.put("status", task.getStatus());
        body.put("responseData", task.getResponseData());
        body.put("createTimeMs", task.getCreateTimeMs());
        body.put("endExecuteTimeMs", task.getEndExecuteTimeMs());
        body.put("executeDurationMs", task.getExecuteDurationMs());
        return body;
    }
}
