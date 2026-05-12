package com.apiflow.domain.group;

import org.springframework.context.ApplicationEvent;

public class GroupChangeEvent extends ApplicationEvent {

    private final String groupNo;
    private final String changeType;

    public GroupChangeEvent(Object source, String groupNo, String changeType) {
        super(source);
        this.groupNo = groupNo;
        this.changeType = changeType;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public String getChangeType() {
        return changeType;
    }

    public static GroupChangeEvent updated(Object source, String groupNo) {
        return new GroupChangeEvent(source, groupNo, "UPDATED");
    }

    public static GroupChangeEvent deleted(Object source, String groupNo) {
        return new GroupChangeEvent(source, groupNo, "DELETED");
    }

    public static GroupChangeEvent created(Object source, String groupNo) {
        return new GroupChangeEvent(source, groupNo, "CREATED");
    }
}