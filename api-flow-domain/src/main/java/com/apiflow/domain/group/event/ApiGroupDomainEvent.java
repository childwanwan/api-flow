package com.apiflow.domain.group.event;

import com.apiflow.domain.shared.event.DomainEvent;
import lombok.Getter;

@Getter
public class ApiGroupDomainEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private static final String AGGREGATE_TYPE = "API_GROUP";

    private final String groupCode;
    private final String groupName;
    private final String groupDescription;
    private final String operator;

    private ApiGroupDomainEvent(String eventType, String groupNo, String groupCode,
                                String groupName, String groupDescription, String operator) {
        super(eventType, AGGREGATE_TYPE, groupNo);
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.operator = operator;
    }

    public String getGroupNo() {
        return getAggregateId();
    }

    public static class Created extends ApiGroupDomainEvent {

        private static final long serialVersionUID = 1L;

        public Created(String groupNo, String groupCode, String groupName,
                       String groupDescription, String operator) {
            super("API_GROUP_CREATED", groupNo, groupCode, groupName, groupDescription, operator);
        }
    }

    public static class Updated extends ApiGroupDomainEvent {

        private static final long serialVersionUID = 1L;

        private final String oldGroupCode;
        private final String oldGroupName;
        private final String oldGroupDescription;

        public Updated(String groupNo, String groupCode, String groupName,
                       String groupDescription, String operator,
                       String oldGroupCode, String oldGroupName, String oldGroupDescription) {
            super("API_GROUP_UPDATED", groupNo, groupCode, groupName, groupDescription, operator);
            this.oldGroupCode = oldGroupCode;
            this.oldGroupName = oldGroupName;
            this.oldGroupDescription = oldGroupDescription;
        }

        public String getOldGroupCode() {
            return oldGroupCode;
        }

        public String getOldGroupName() {
            return oldGroupName;
        }

        public String getOldGroupDescription() {
            return oldGroupDescription;
        }

    }

    public static class Deleted extends ApiGroupDomainEvent {

        private static final long serialVersionUID = 1L;

        public Deleted(String groupNo, String operator) {
            super("API_GROUP_DELETED", groupNo, null, null, null, operator);
        }
    }
}
