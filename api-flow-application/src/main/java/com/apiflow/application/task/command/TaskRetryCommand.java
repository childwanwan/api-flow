package com.apiflow.application.task.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRetryCommand {
    private String taskNo;
    private String retryOperator;
}
