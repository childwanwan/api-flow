package com.flow.api.mq;

import java.util.function.Consumer;

public interface MessageConsumer {

    void subscribe(String topic, Consumer<String> handler);

    void subscribe(String topic, Class<?> payloadType, Consumer<Object> handler);
}
