package com.apiflow.infrastructure.mq;

import com.apiflow.api.mq.MessageConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessageConsumer implements MessageConsumer {

    private final RedisMessageListenerContainer listenerContainer;
    private final Map<String, MessageListener> listeners = new ConcurrentHashMap<>();

    @Override
    public void subscribe(String topic, Consumer<String> handler) {
        MessageListener listener = (Message message, byte[] pattern) -> {
            try {
                String body = new String(message.getBody());
                handler.accept(body);
            } catch (Exception e) {
                log.error("Error processing message from topic: {}", topic, e);
            }
        };
        listeners.put(topic, listener);
        listenerContainer.addMessageListener(listener, new ChannelTopic(topic));
        log.info("Subscribed to topic: {}", topic);
    }

    @Override
    public void subscribe(String topic, Class<?> payloadType, Consumer<Object> handler) {
        subscribe(topic, message -> {
            try {
                handler.accept(message);
            } catch (Exception e) {
                log.error("Error processing typed message from topic: {}", topic, e);
            }
        });
    }
}
