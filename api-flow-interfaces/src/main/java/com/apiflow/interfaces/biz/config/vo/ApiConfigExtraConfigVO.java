package com.apiflow.interfaces.biz.config.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigExtraConfigVO {
    private Map<String, String> envConfig;
    private String targetUrl;
    private String targetMethod;
    private Map<String, String> targetHeaders;
    private String targetBodyTemplate;
    private Integer targetTimeoutMs;
}
