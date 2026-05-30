package com.apiflow.application.shared.event;

import com.apiflow.api.mq.IntegrationEvent;
import com.apiflow.domain.shared.event.DomainEvent;
import com.apiflow.domain.shared.model.AggregateRoot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DomainEventConverterFacade {

    private final List<DomainIntegrationEventConverter> converters;

    public List<IntegrationEvent> convert(AggregateRoot aggregate) {
        List<DomainEvent> domainEvents = new ArrayList<>(aggregate.getDomainEvents());
        aggregate.clearDomainEvents();

        List<IntegrationEvent> integrationEvents = new ArrayList<>();
        for (DomainEvent domainEvent : domainEvents) {
            DomainIntegrationEventConverter converter = resolveConverter(domainEvent);
            if (converter != null) {
                integrationEvents.add(converter.convert(domainEvent));
            }
        }
        return integrationEvents;
    }

    private DomainIntegrationEventConverter resolveConverter(DomainEvent event) {
        for (DomainIntegrationEventConverter converter : converters) {
            if (converter.supports(event)) {
                return converter;
            }
        }
        return null;
    }
}
