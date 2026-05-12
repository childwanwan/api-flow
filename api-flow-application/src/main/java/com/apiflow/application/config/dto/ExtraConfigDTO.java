package com.apiflow.application.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtraConfigDTO {

    private String region;
    private String sellerId;
    private String awsAccessKey;
    private String environment;
    private String targetUrl;
    private String targetMethod;
    private Map<String, String> targetHeaders;
    private String targetBodyTemplate;
    private Integer targetTimeoutMs;
}
