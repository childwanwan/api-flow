package com.apiflow.domain.scheduler;

import com.apiflow.domain.shared.event.DomainEvent;

public class TaskChangeEvent extends DomainEvent {

    private final String taskNo;
    private final String changeType;

    public TaskChangeEvent(String taskNo, String changeType) {
        super(changeType, "TASK", taskNo);
        this.taskNo = taskNo;
        this.changeType = changeType;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public String getChangeType() {
        return changeType;
    }

    public static TaskChangeEvent submitted(String taskNo) {
        return new TaskChangeEvent(taskNo, "SUBMITTED");
    }

    public static TaskChangeEvent retried(String taskNo) {
        return new TaskChangeEvent(taskNo, "RETRIED");
    }
}
