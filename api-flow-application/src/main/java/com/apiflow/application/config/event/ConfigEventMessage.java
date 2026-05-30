package com.apiflow.application.config.event;

import com.apiflow.api.mq.IntegrationEvent;

public record ConfigEventMessage(
        EventHeader header,
        ConfigEventData data
) implements IntegrationEvent {

    private static final String TOPIC = "api-config-event";

    @Override
    public String eventId() {
        return header.eventId();
    }

    @Override
    public String eventType() {
        return header.eventType();
    }

    @Override
    public String aggregateType() {
        return header.aggregateType();
    }

    @Override
    public String aggregateId() {
        return header.aggregateId();
    }

    @Override
    public long occurredOnMs() {
        return header.occurredOnMs();
    }

    @Override
    public String topic() {
        return TOPIC;
    }

    public record EventHeader(
            String eventId,
            String eventType,
            String aggregateType,
            String aggregateId,
            long occurredOnMs
    ) {}

    public record ConfigEventData(
            String apiCode,
            String apiName,
            String operator
    ) {}
}
