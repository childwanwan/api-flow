package com.apiflow.domain.shared.event;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public abstract class DomainEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String eventId;
    private final String eventType;
    private final String aggregateType;
    private final String aggregateId;
    private final long occurredOnMs;

    protected DomainEvent(String eventType, String aggregateType, String aggregateId) {
        this.eventId = UUID.randomUUID().toString().replace("-", "");
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.occurredOnMs = System.currentTimeMillis();
    }
}
