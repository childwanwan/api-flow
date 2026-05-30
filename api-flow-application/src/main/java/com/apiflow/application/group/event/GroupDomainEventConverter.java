package com.apiflow.application.group.event;

import com.apiflow.api.mq.IntegrationEvent;
import com.apiflow.application.shared.event.DomainIntegrationEventConverter;
import com.apiflow.domain.group.event.ApiGroupDomainEvent;
import com.apiflow.domain.shared.event.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class GroupDomainEventConverter implements DomainIntegrationEventConverter {

    @Override
    public boolean supports(DomainEvent event) {
        return event instanceof ApiGroupDomainEvent;
    }

    @Override
    public IntegrationEvent convert(DomainEvent event) {
        ApiGroupDomainEvent groupEvent = (ApiGroupDomainEvent) event;
        GroupEventMessage.Snapshot snapshot = null;
        if (groupEvent instanceof ApiGroupDomainEvent.Updated updated) {
            snapshot = new GroupEventMessage.Snapshot(
                    updated.getOldGroupCode(),
                    updated.getOldGroupName(),
                    updated.getOldGroupDescription()
            );
        }
        GroupEventMessage.EventHeader header = new GroupEventMessage.EventHeader(
                groupEvent.getEventId(),
                groupEvent.getEventType(),
                groupEvent.getAggregateType(),
                groupEvent.getAggregateId(),
                groupEvent.getOccurredOnMs()
        );
        GroupEventMessage.GroupEventData data = new GroupEventMessage.GroupEventData(
                groupEvent.getGroupCode(),
                groupEvent.getGroupName(),
                groupEvent.getGroupDescription(),
                groupEvent.getOperator()
        );
        return new GroupEventMessage(header, data, snapshot);
    }
}
