package com.apiflow.infrastructure.event;

import com.apiflow.api.mq.IntegrationEvent;
import com.apiflow.api.mq.MessageProducer;
import com.apiflow.domain.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "api-flow.mq.type", havingValue = "kafka")
public class MqDomainEventPublisher implements DomainEventPublisher {

    private static final int MAX_RETRY = 3;

    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;

    @Override
    public void publishAll(List<IntegrationEvent> events) {
        for (IntegrationEvent event : events) {
            publish(event);
        }
    }

    @Override
    public void publish(IntegrationEvent event) {
        sendToMQWithRetry(event);
    }

    private void sendToMQWithRetry(IntegrationEvent event) {
        String json = serializeEvent(event);
        if (json == null) {
            return;
        }
        String topic = event.topic();
        Map<String, String> headers = new HashMap<>();
        headers.put("eventType", event.eventType());
        headers.put("aggregateType", event.aggregateType());
        headers.put("eventId", event.eventId());

        for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {
            try {
                messageProducer.send(topic, json, headers);
                log.info("Sent integration event to MQ: type={}, eventId={}, topic={}, attempt={}",
                        event.eventType(), event.eventId(), topic, attempt);
                return;
            } catch (Exception e) {
                log.warn("Failed to send event to MQ: type={}, topic={}, attempt={}/{}",
                        event.eventType(), topic, attempt, MAX_RETRY, e);
                if (attempt < MAX_RETRY) {
                    sleep(attempt);
                }
            }
        }
        log.error("[MQ_COMPENSATE_REQUIRED] All retries exhausted: type={}, eventId={}, topic={}",
                event.eventType(), event.eventId(), topic);
    }

    private String serializeEvent(IntegrationEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            log.error("Failed to serialize integration event: type={}, eventId={}",
                    event.eventType(), event.eventId(), e);
            return null;
        }
    }

    private void sleep(int attempt) {
        try {
            Thread.sleep(100L * (1L << attempt));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
