package com.apigateway.interfaces.config.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigCreateRequest {

    private String groupNo;

    @NotBlank(message = "apiCode cannot be blank")
    private String apiCode;

    @NotBlank(message = "apiName cannot be blank")
    private String apiName;

    private String apiDescription;

    private Long requestTimeoutMs;

    private Integer autoRetryCount;

    private Long retryIntervalMs;

    private String rateLimitConfig;

    private Integer maxQueueSize;

    private String filterRules;

    private String pluginConfig;

    private String receiptConfig;

    private String extraConfig;

    private String operator;

}
