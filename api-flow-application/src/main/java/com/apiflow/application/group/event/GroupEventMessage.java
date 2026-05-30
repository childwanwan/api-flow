package com.apiflow.application.group.event;

import com.apiflow.api.mq.IntegrationEvent;

public record GroupEventMessage(
        EventHeader header,
        GroupEventData data,
        Snapshot snapshot
) implements IntegrationEvent {

    private static final String TOPIC = "api-group-event";

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

    public record GroupEventData(
            String groupCode,
            String groupName,
            String groupDescription,
            String operator
    ) {}

    public record Snapshot(
            String groupCode,
            String groupName,
            String groupDescription
    ) {}
}
