package com.apiflow.domain.shared.event;

import com.apiflow.api.mq.IntegrationEvent;

import java.util.List;

public interface DomainEventPublisher {

    void publishAll(List<IntegrationEvent> events);

    void publish(IntegrationEvent event);
}
