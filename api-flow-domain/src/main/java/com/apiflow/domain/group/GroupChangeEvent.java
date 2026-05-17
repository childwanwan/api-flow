package com.apiflow.domain.group;

import com.apiflow.domain.shared.event.DomainEvent;

public class GroupChangeEvent extends DomainEvent {

    private final String groupNo;
    private final String changeType;

    public GroupChangeEvent(String groupNo, String changeType) {
        super(changeType, "GROUP", groupNo);
        this.groupNo = groupNo;
        this.changeType = changeType;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public String getChangeType() {
        return changeType;
    }

    public static GroupChangeEvent updated(String groupNo) {
        return new GroupChangeEvent(groupNo, "UPDATED");
    }

    public static GroupChangeEvent deleted(String groupNo) {
        return new GroupChangeEvent(groupNo, "DELETED");
    }

    public static GroupChangeEvent created(String groupNo) {
        return new GroupChangeEvent(groupNo, "CREATED");
    }
}
