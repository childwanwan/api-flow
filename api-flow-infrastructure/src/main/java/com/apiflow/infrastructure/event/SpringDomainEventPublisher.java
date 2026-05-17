package com.apiflow.infrastructure.event;

import com.apiflow.domain.shared.event.DomainEvent;
import com.apiflow.domain.shared.event.DomainEventPublisher;
import com.apiflow.domain.shared.model.AggregateRoot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishAll(AggregateRoot aggregate) {
        List<DomainEvent> events = new ArrayList<>(aggregate.getDomainEvents());
        aggregate.clearDomainEvents();
        for (DomainEvent event : events) {
            try {
                applicationEventPublisher.publishEvent(event);
            } catch (Exception e) {
                log.error("Failed to publish domain event: type={}, eventId={}",
                        event.getEventType(), event.getEventId(), e);
            }
        }
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            applicationEventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.error("Failed to publish domain event: type={}, eventId={}",
                    event.getEventType(), event.getEventId(), e);
        }
    }
}
