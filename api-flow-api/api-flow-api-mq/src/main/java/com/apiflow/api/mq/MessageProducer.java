package com.apiflow.api.mq;

import java.util.Map;

public interface MessageProducer {

    void send(String topic, String message);

    void send(String topic, String message, Map<String, String> headers);

    void send(String topic, Object payload);

    void send(String topic, Object payload, Map<String, String> headers);
}
