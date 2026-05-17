package com.apiflow.interfaces.biz.config.request;

import com.apiflow.interfaces.util.ValidationHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigExtraConfigRequest {

    private Map<String, String> envConfig;
    private String targetUrl;
    private String targetMethod;
    private Map<String, String> targetHeaders;
    private String targetBodyTemplate;
    private Integer targetTimeoutMs;

    public void validate() {
        if (envConfig != null) {
            envConfig.forEach((key, value) -> {
                ValidationHelper.validateSize(key, 128, "envConfig.key");
                ValidationHelper.validateSize(value, 512, "envConfig.value");
            });
        }
        ValidationHelper.validateSize(targetUrl, 2048, "targetUrl");
        if (targetMethod != null) {
            ValidationHelper.validatePattern(targetMethod, "^(GET|POST|PUT|DELETE|PATCH)$", "targetMethod");
        }
        ValidationHelper.validateSize(targetBodyTemplate, 8192, "targetBodyTemplate");
        if (targetTimeoutMs != null) {
            ValidationHelper.validateRange(targetTimeoutMs.longValue(), 100L, 300000L, "targetTimeoutMs");
        }
    }
}
