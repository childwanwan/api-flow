package com.apiflow.application.task.param;

import com.apiflow.application.task.dto.ReceiptConfigDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitTaskParam {
    private String apiCode;
    private String source;
    private String groupNo;
    private String actionType;
    private Integer priority;
    private Map<String, Object> params;
    private Map<String, Object> customData;
    private ReceiptConfigDTO receiptConfig;
}
