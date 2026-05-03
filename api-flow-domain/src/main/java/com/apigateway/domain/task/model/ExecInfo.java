package com.apigateway.domain.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecInfo {

    @Builder.Default
    private List<PluginStep> steps = new ArrayList<>();
    private Long totalCostTimeMs;
    private Integer retryCount;

    public List<PluginStep> getSteps() {
        return steps == null ? Collections.emptyList() : steps;
    }

    public void addStep(PluginStep step) {
        if (steps == null) {
            steps = new ArrayList<>();
        }
        steps.add(step);
    }

}
