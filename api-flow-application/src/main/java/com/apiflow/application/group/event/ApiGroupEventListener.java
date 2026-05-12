package com.apiflow.application.group.event;

import com.apiflow.api.mq.MessageProducer;
import com.apiflow.domain.group.event.ApiGroupDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "api-flow.mq.type", havingValue = "redis", matchIfMissing = true)
public class ApiGroupEventListener {

    private static final String TOPIC = "api-group-event";
    private static final int MAX_RETRY = 3;

    private final GroupEventHandler groupEventHandler;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;

    @EventListener
    public void onGroupCreated(ApiGroupDomainEvent.Created event) {
        groupEventHandler.handleCreated(toMessage(event));
        sendToMQWithRetry(event);
    }

    @EventListener
    public void onGroupUpdated(ApiGroupDomainEvent.Updated event) {
        groupEventHandler.handleUpdated(toMessage(event));
        sendToMQWithRetry(event);
    }

    @EventListener
    public void onGroupDeleted(ApiGroupDomainEvent.Deleted event) {
        groupEventHandler.handleDeleted(toMessage(event));
        sendToMQWithRetry(event);
    }

    private GroupEventMessage toMessage(ApiGroupDomainEvent event) {
        String oldGroupCode = null;
        String oldGroupName = null;
        String oldGroupDescription = null;
        if (event instanceof ApiGroupDomainEvent.Updated updated) {
            oldGroupCode = updated.getOldGroupCode();
            oldGroupName = updated.getOldGroupName();
            oldGroupDescription = updated.getOldGroupDescription();
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
                oldGroupCode,
                oldGroupName,
                oldGroupDescription
        );
    }

    private void sendToMQWithRetry(ApiGroupDomainEvent event) {
        String json = serializeEvent(event);
        if (json == null) {
            return;
        }

        for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {
            try {
                messageProducer.send(TOPIC, json);
                log.info("Sent group event to MQ: type={}, groupNo={}, attempt={}",
                        event.getEventType(), event.getGroupNo(), attempt);
                return;
            } catch (Exception e) {
                log.warn("Failed to send group event to MQ: type={}, groupNo={}, attempt={}/{}",
                        event.getEventType(), event.getGroupNo(), attempt, MAX_RETRY, e);
                if (attempt < MAX_RETRY) {
                    sleep(attempt);
                }
            }
        }

        log.error("[MQ_COMPENSATE_REQUIRED] All retries exhausted for event: type={}, groupNo={}, eventId={}, payload={}",
                event.getEventType(), event.getGroupNo(), event.getEventId(), json);
    }

    private String serializeEvent(ApiGroupDomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            log.error("Failed to serialize event: type={}, groupNo={}, eventId={}",
                    event.getEventType(), event.getGroupNo(), event.getEventId(), e);
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
