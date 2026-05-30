package com.apiflow.application.shared.event;

import com.apiflow.api.mq.IntegrationEvent;
import com.apiflow.domain.shared.event.DomainEvent;

public interface DomainIntegrationEventConverter {

    boolean supports(DomainEvent event);

    IntegrationEvent convert(DomainEvent event);
}
