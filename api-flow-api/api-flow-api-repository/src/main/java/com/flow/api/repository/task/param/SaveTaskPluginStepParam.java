package com.flow.api.repository.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveTaskPluginStepParam {
    private Integer stepOrder;
    private String pluginCode;
    private String pluginName;
    private String status;
    private Object inputData;
    private Object outputData;
    private Long executeStartTimeMs;
    private Long executeEndTimeMs;
    private Long executeCostTimeMs;
    private Object compensateData;
}
