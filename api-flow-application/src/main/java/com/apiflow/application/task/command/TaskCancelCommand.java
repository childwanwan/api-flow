package com.apiflow.application.task.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskCancelCommand {
    private String taskNo;
    private String reason;
    private String canceledBy;
}
