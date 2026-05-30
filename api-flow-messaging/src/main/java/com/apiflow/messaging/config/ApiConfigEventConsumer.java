package com.apiflow.messaging.config;

import com.apiflow.application.config.event.ConfigEventHandler;
import com.apiflow.application.config.event.ConfigEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "api-flow.mq.type", havingValue = "kafka")
public class ApiConfigEventConsumer {

    private final ConfigEventHandler configEventHandler;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "api-config-event",
            groupId = "api-flow-config-consumer"
    )
    public void onConfigEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String eventType = resolveEventType(record);
        String json = record.value();

        try {
            ConfigEventMessage msg = objectMapper.readValue(json, ConfigEventMessage.class);
            dispatch(msg);
            ack.acknowledge();
            log.info("Processed config event from Kafka: type={}, aggregateId={}", eventType, msg.aggregateId());
        } catch (Exception e) {
            log.error("Failed to process config event from Kafka: type={}, offset={}", eventType, record.offset(), e);
            throw e;
        }
    }

    private void dispatch(ConfigEventMessage msg) {
        switch (msg.eventType()) {
            case "API_CONFIG_CREATED" -> configEventHandler.handleCreated(msg);
            case "API_CONFIG_UPDATED" -> configEventHandler.handleUpdated(msg);
            case "API_CONFIG_DELETED" -> configEventHandler.handleDeleted(msg);
            case "API_CONFIG_ENABLED" -> configEventHandler.handleEnabled(msg);
            case "API_CONFIG_DISABLED" -> configEventHandler.handleDisabled(msg);
            default -> log.warn("Unknown config event type: {}", msg.eventType());
        }
    }

    private String resolveEventType(ConsumerRecord<String, String> record) {
        var header = record.headers().lastHeader("eventType");
        if (header != null) {
            return new String(header.value(), StandardCharsets.UTF_8);
        }
        try {
            var node = objectMapper.readTree(record.value());
            if (node.has("header") && node.get("header").has("eventType")) {
                return node.get("header").get("eventType").asText();
            }
            return "UNKNOWN";
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
}
