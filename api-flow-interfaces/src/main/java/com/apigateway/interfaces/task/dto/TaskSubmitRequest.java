package com.apigateway.interfaces.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "apiCode cannot be blank")
    private String apiCode;

    @NotBlank(message = "source cannot be blank")
    private String source;

    private String groupNo;

    private String actionType;

    private Integer priority;

    @NotNull(message = "params cannot be null")
    private Map<String, Object> params;

    private Map<String, Object> customData;

    private String receiptConfig;

}
