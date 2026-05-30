package com.apiflow.application.config.event;

import com.apiflow.api.mq.IntegrationEvent;
import com.apiflow.application.shared.event.DomainIntegrationEventConverter;
import com.apiflow.domain.config.event.ApiConfigDomainEvent;
import com.apiflow.domain.shared.event.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class ConfigDomainEventConverter implements DomainIntegrationEventConverter {

    @Override
    public boolean supports(DomainEvent event) {
        return event instanceof ApiConfigDomainEvent;
    }

    @Override
    public IntegrationEvent convert(DomainEvent event) {
        ApiConfigDomainEvent configEvent = (ApiConfigDomainEvent) event;
        ConfigEventMessage.EventHeader header = new ConfigEventMessage.EventHeader(
                configEvent.getEventId(),
                configEvent.getEventType(),
                configEvent.getAggregateType(),
                configEvent.getAggregateId(),
                configEvent.getOccurredOnMs()
        );
        ConfigEventMessage.ConfigEventData data = new ConfigEventMessage.ConfigEventData(
                configEvent.getApiCode(),
                configEvent.getApiName(),
                configEvent.getOperator()
        );
        return new ConfigEventMessage(header, data);
    }
}
