package com.apiflow.application.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecInfoDTO {

    private List<PluginStepDTO> steps;
    private Long totalCostTimeMs;
    private Integer retryCount;
}
