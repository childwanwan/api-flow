package com.apiflow.application.config.event;

public record ConfigEventMessage(
        String eventId,
        String eventType,
        String aggregateType,
        String aggregateId,
        long occurredOnMs,
        String apiCode,
        String apiName,
        String operator
) {}
