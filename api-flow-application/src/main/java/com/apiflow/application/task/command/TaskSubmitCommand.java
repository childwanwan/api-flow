package com.apiflow.application.task.command;

import com.apiflow.application.task.dto.ReceiptConfigDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class TaskSubmitCommand {
    private String apiCode;
    private String source;
    private String groupNo;
    private String actionType;
    private Integer priority;
    private Map<String, Object> params;
    private Map<String, Object> customData;
    private ReceiptConfigDTO receiptConfig;
}
