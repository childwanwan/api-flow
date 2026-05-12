package com.apiflow.infrastructure.mq;

import com.apiflow.api.mq.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "api-flow.mq.type", havingValue = "kafka")
public class KafkaMessageProducer implements MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void send(String topic, String message) {
        send(topic, message, null);
    }

    @Override
    public void send(String topic, String message, Map<String, String> headers) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
            if (headers != null) {
                headers.forEach((key, value) ->
                        record.headers().add(key, value.getBytes(StandardCharsets.UTF_8))
                );
            }
            kafkaTemplate.send(record).get(10, TimeUnit.SECONDS);
            log.debug("Message sent to Kafka topic: {}", topic);
        } catch (Exception e) {
            log.error("Failed to send message to Kafka topic: {}", topic, e);
            throw new RuntimeException("Failed to send message to Kafka", e);
        }
    }

    @Override
    public void send(String topic, Object payload) {
        send(topic, payload, null);
    }

    @Override
    public void send(String topic, Object payload, Map<String, String> headers) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            send(topic, json, headers);
        } catch (Exception e) {
            log.error("Failed to serialize payload for topic: {}", topic, e);
            throw new RuntimeException("Failed to serialize payload", e);
        }
    }
}
