package com.apiflow.api.mq;

public interface IntegrationEvent {

    String eventId();

    String eventType();

    String aggregateType();

    String aggregateId();

    long occurredOnMs();

    String topic();
}
