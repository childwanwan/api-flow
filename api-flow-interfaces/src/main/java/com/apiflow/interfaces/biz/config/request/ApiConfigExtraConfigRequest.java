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

    private String region;
    private String sellerId;
    private String awsAccessKey;
    private String environment;
    private String targetUrl;
    private String targetMethod;
    private Map<String, String> targetHeaders;
    private String targetBodyTemplate;
    private Integer targetTimeoutMs;

    public void validate() {
        ValidationHelper.validateSize(region, 128, "region");
        ValidationHelper.validateSize(sellerId, 128, "sellerId");
        ValidationHelper.validateSize(awsAccessKey, 256, "awsAccessKey");
        ValidationHelper.validateSize(environment, 64, "environment");
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
