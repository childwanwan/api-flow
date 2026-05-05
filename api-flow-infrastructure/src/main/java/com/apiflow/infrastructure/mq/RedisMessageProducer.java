package com.apiflow.infrastructure.mq;

import com.apiflow.api.mq.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessageProducer implements MessageProducer {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void send(String topic, String message) {
        try {
            redisTemplate.convertAndSend(topic, message);
            log.debug("Message sent to topic: {}", topic);
        } catch (Exception e) {
            log.error("Failed to send message to topic: {}", topic, e);
            throw new RuntimeException("Failed to send message", e);
        }
    }

    @Override
    public void send(String topic, String message, Map<String, String> headers) {
        send(topic, message);
    }

    @Override
    public void send(String topic, Object payload) {
        send(topic, payload != null ? payload.toString() : null);
    }

    @Override
    public void send(String topic, Object payload, Map<String, String> headers) {
        send(topic, payload);
    }
}
