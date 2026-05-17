package com.apiflow.application.group.event;

public record GroupEventMessage(
        String eventId,
        String eventType,
        String aggregateType,
        String aggregateId,
        long occurredOnMs,
        String groupCode,
        String groupName,
        String groupDescription,
        String operator,
        GroupEventMessage.Snapshot snapshot
) {
    public record Snapshot(
            String groupCode,
            String groupName,
            String groupDescription
    ) {}
}
