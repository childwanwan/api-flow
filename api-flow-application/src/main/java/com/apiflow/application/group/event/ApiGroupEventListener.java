package com.apiflow.application.group.event;

import com.apiflow.api.mq.MessageProducer;
import com.apiflow.domain.group.event.ApiGroupDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiGroupEventListener {

    private static final String TOPIC = "api-group-event";
    private static final int MAX_RETRY = 3;

    private final GroupEventHandler groupEventHandler;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;

    @EventListener
    public void onGroupCreated(ApiGroupDomainEvent.Created event) {
        GroupEventMessage msg = toMessage(event);
        groupEventHandler.handleCreated(msg);
        sendToMQWithRetry(msg);
    }

    @EventListener
    public void onGroupUpdated(ApiGroupDomainEvent.Updated event) {
        GroupEventMessage msg = toMessage(event);
        groupEventHandler.handleUpdated(msg);
        sendToMQWithRetry(msg);
    }

    @EventListener
    public void onGroupDeleted(ApiGroupDomainEvent.Deleted event) {
        GroupEventMessage msg = toMessage(event);
        groupEventHandler.handleDeleted(msg);
        sendToMQWithRetry(msg);
    }

    private GroupEventMessage toMessage(ApiGroupDomainEvent event) {
        GroupEventMessage.Snapshot snapshot = null;
        if (event instanceof ApiGroupDomainEvent.Updated updated) {
            snapshot = new GroupEventMessage.Snapshot(
                    updated.getOldGroupCode(),
                    updated.getOldGroupName(),
                    updated.getOldGroupDescription()
            );
        }
        return new GroupEventMessage(
                event.getEventId(),
                event.getEventType(),
                event.getAggregateType(),
                event.getAggregateId(),
                event.getOccurredOnMs(),
                event.getGroupCode(),
                event.getGroupName(),
                event.getGroupDescription(),
                event.getOperator(),
                snapshot
        );
    }

    private void sendToMQWithRetry(GroupEventMessage msg) {
        String json = serializeMessage(msg);
        if (json == null) {
            return;
        }

        for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {
            try {
                messageProducer.send(TOPIC, json);
                log.info("Sent group event to MQ: type={}, groupNo={}, attempt={}",
                        msg.eventType(), msg.aggregateId(), attempt);
                return;
            } catch (Exception e) {
                log.warn("Failed to send group event to MQ: type={}, groupNo={}, attempt={}/{}",
                        msg.eventType(), msg.aggregateId(), attempt, MAX_RETRY, e);
                if (attempt < MAX_RETRY) {
                    sleep(attempt);
                }
            }
        }

        log.error("[MQ_COMPENSATE_REQUIRED] All retries exhausted for event: type={}, aggregateId={}, eventId={}",
                msg.eventType(), msg.aggregateId(), msg.eventId());
    }

    private String serializeMessage(GroupEventMessage msg) {
        try {
            return objectMapper.writeValueAsString(msg);
        } catch (Exception e) {
            log.error("Failed to serialize event message: type={}, aggregateId={}, eventId={}",
                    msg.eventType(), msg.aggregateId(), msg.eventId(), e);
            return null;
        }
    }

    private void sleep(int attempt) {
        try {
            Thread.sleep(100L * (1L << attempt));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
