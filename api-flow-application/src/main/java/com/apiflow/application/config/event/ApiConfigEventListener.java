package com.apiflow.application.config.event;

import com.apiflow.domain.config.event.ApiConfigDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiConfigEventListener {

    private final ConfigEventHandler configEventHandler;

    @EventListener
    public void onConfigCreated(ApiConfigDomainEvent.Created event) {
        ConfigEventMessage msg = toMessage(event);
        configEventHandler.handleCreated(msg);
    }

    @EventListener
    public void onConfigUpdated(ApiConfigDomainEvent.Updated event) {
        ConfigEventMessage msg = toMessage(event);
        configEventHandler.handleUpdated(msg);
    }

    @EventListener
    public void onConfigDeleted(ApiConfigDomainEvent.Deleted event) {
        ConfigEventMessage msg = toMessage(event);
        configEventHandler.handleDeleted(msg);
    }

    @EventListener
    public void onConfigEnabled(ApiConfigDomainEvent.Enabled event) {
        ConfigEventMessage msg = toMessage(event);
        configEventHandler.handleEnabled(msg);
    }

    @EventListener
    public void onConfigDisabled(ApiConfigDomainEvent.Disabled event) {
        ConfigEventMessage msg = toMessage(event);
        configEventHandler.handleDisabled(msg);
    }

    private ConfigEventMessage toMessage(ApiConfigDomainEvent event) {
        return new ConfigEventMessage(
                event.getEventId(),
                event.getEventType(),
                event.getAggregateType(),
                event.getAggregateId(),
                event.getOccurredOnMs(),
                event.getApiCode(),
                event.getApiName(),
                event.getOperator()
        );
    }
}
