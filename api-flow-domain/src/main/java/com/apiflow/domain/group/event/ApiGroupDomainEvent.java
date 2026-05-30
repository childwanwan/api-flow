package com.apiflow.domain.group.event;

import com.apiflow.domain.shared.event.DomainEvent;
import lombok.Getter;

@Getter
public class ApiGroupDomainEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private static final String AGGREGATE_TYPE = "API_GROUP";

    private final ApiGroupEventData data;

    private ApiGroupDomainEvent(String eventType, ApiGroupEventData data) {
        super(eventType, AGGREGATE_TYPE, data.getGroupNo());
        this.data = data;
    }

    public String getGroupNo() {
        return data.getGroupNo();
    }

    public String getGroupCode() {
        return data.getGroupCode();
    }

    public String getGroupName() {
        return data.getGroupName();
    }

    public String getGroupDescription() {
        return data.getGroupDescription();
    }

    public String getOperator() {
        return data.getOperator();
    }

    public static class Created extends ApiGroupDomainEvent {

        private static final long serialVersionUID = 1L;

        public Created(ApiGroupEventData data) {
            super("API_GROUP_CREATED", data);
        }
    }

    public static class Updated extends ApiGroupDomainEvent {

        private static final long serialVersionUID = 1L;

        private final ApiGroupEventData oldData;

        public Updated(ApiGroupEventData data, ApiGroupEventData oldData) {
            super("API_GROUP_UPDATED", data);
            this.oldData = oldData;
        }

        public String getOldGroupCode() {
            return oldData.getGroupCode();
        }

        public String getOldGroupName() {
            return oldData.getGroupName();
        }

        public String getOldGroupDescription() {
            return oldData.getGroupDescription();
        }
    }

    public static class Deleted extends ApiGroupDomainEvent {

        private static final long serialVersionUID = 1L;

        public Deleted(ApiGroupEventData data) {
            super("API_GROUP_DELETED", data);
        }
    }
}
