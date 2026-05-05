package com.apiflow.interfaces.biz.task.request;

import com.apiflow.application.task.dto.ReceiptConfigDTO;
import com.apiflow.interfaces.util.ValidationHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskSubmitRequest {

    private String apiCode;
    private String source;
    private String groupNo;
    private String actionType;
    private Integer priority;
    private Map<String, Object> params;
    private Map<String, Object> customData;
    private ReceiptConfigDTO receiptConfig;

    public void validate() {
        ValidationHelper.validateNotBlank(apiCode, "apiCode");
        ValidationHelper.validateSize(apiCode, 128, "apiCode");
        ValidationHelper.validatePattern(apiCode, "^[a-zA-Z0-9_-]+$", "apiCode");
        ValidationHelper.validateNotBlank(source, "source");
        ValidationHelper.validateSize(source, 64, "source");
        ValidationHelper.validateSize(groupNo, 64, "groupNo");
        ValidationHelper.validateSize(actionType, 32, "actionType");
        ValidationHelper.validateRange(priority, 1, 10, "priority");
        ValidationHelper.validateNotNull(params, "params");
        ValidationHelper.validateMapSize(params, 1000, "params");
        ValidationHelper.validateMapSize(customData, 100, "customData");
    }
}
