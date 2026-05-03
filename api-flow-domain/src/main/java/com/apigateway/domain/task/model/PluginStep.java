package com.apigateway.domain.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginStep {

    private Integer stepOrder;
    private String pluginCode;
    private String pluginName;
    private String status;
    private Object inputData;
    private Object outputData;
    private Long executeStartTimeMs;
    private Long executeEndTimeMs;
    private Long executeCostTimeMs;
    private String errorMessage;
    private Map<String, Object> compensateData;

}
