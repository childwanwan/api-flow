package com.apiflow.domain.receipt;

import com.apiflow.api.mq.MessageProducer;
import com.apiflow.domain.task.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class ReceiptService {

    private final MessageProducer messageProducer;
    private final HttpReceiptGateway httpReceiptGateway;

    public void sendReceipt(Task task, ReceiptConfig receiptConfig) {
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

    public void sendHttpReceipt(Task task, HttpReceipt httpReceipt) {
        if (httpReceipt == null || httpReceipt.getUrl() == null) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            int maxRetries = httpReceipt.getRetryPolicy() != null && httpReceipt.getRetryPolicy().getMaxRetries() != null
                    ? httpReceipt.getRetryPolicy().getMaxRetries() : 64;
            long baseInterval = 1000;

            for (int attempt = 0; attempt <= maxRetries; attempt++) {
                try {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    if (httpReceipt.getHeaders() != null) {
                        headers.putAll(httpReceipt.getHeaders());
                    }
                    headers.put("X-Request-Id", task.getTaskNo() + "-receipt-" + attempt);

                    Map<String, Object> body = buildReceiptBody(task);

                    int statusCode = httpReceiptGateway.sendHttpRequest(
                            httpReceipt.getUrl(),
                            httpReceipt.getMethod() != null ? httpReceipt.getMethod() : "POST",
                            headers,
                            body
                    );

                    if (statusCode >= 200 && statusCode < 300) {
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

    public void sendMqReceipt(Task task, MqReceipt mqReceipt) {
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

    private Map<String, Object> buildReceiptBody(Task task) {
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
