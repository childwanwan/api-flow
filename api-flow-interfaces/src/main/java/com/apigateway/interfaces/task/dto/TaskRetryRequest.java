package com.apigateway.interfaces.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRetryRequest {

    @NotBlank(message = "taskNo cannot be blank")
    private String taskNo;

    private String retryOperator;

}
