package com.flow.api.repository.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskExecInfoParam {
    private List<UpdateTaskPluginStepParam> steps;
    private Long totalCostTimeMs;
    private Integer retryCount;
}
