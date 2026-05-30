package com.apiflow.messaging.group;

import com.apiflow.application.group.event.GroupEventHandler;
import com.apiflow.application.group.event.GroupEventMessage;
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
public class ApiGroupEventConsumer {

    private final GroupEventHandler groupEventHandler;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "api-group-event",
            groupId = "api-flow-group-consumer"
    )
    public void onGroupEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String eventType = resolveEventType(record);
        String json = record.value();

        try {
            GroupEventMessage msg = objectMapper.readValue(json, GroupEventMessage.class);
            dispatch(msg);
            ack.acknowledge();
            log.info("Processed group event from Kafka: type={}, aggregateId={}", eventType, msg.aggregateId());
        } catch (Exception e) {
            log.error("Failed to process group event from Kafka: type={}, offset={}", eventType, record.offset(), e);
            throw e;
        }
    }

    private void dispatch(GroupEventMessage msg) {
        switch (msg.eventType()) {
            case "API_GROUP_CREATED" -> groupEventHandler.handleCreated(msg);
            case "API_GROUP_UPDATED" -> groupEventHandler.handleUpdated(msg);
            case "API_GROUP_DELETED" -> groupEventHandler.handleDeleted(msg);
            default -> log.warn("Unknown group event type: {}", msg.eventType());
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
