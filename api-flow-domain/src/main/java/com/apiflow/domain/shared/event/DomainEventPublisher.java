package com.apiflow.domain.shared.event;

import com.apiflow.domain.shared.model.AggregateRoot;

public interface DomainEventPublisher {

    void publishAll(AggregateRoot aggregate);

    void publish(DomainEvent event);
}
