package com.apiflow.domain.scheduler;

import org.springframework.context.ApplicationEvent;

public class TaskChangeEvent extends ApplicationEvent {

    private final String taskNo;
    private final String changeType;

    public TaskChangeEvent(Object source, String taskNo, String changeType) {
        super(source);
        this.taskNo = taskNo;
        this.changeType = changeType;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public String getChangeType() {
        return changeType;
    }

    public static TaskChangeEvent submitted(Object source, String taskNo) {
        return new TaskChangeEvent(source, taskNo, "SUBMITTED");
    }

    public static TaskChangeEvent retried(Object source, String taskNo) {
        return new TaskChangeEvent(source, taskNo, "RETRIED");
    }
}
