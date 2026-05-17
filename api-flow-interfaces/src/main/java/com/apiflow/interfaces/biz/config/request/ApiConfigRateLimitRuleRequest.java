package com.apiflow.interfaces.biz.config.request;

import com.apiflow.interfaces.util.ValidationHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigRateLimitRuleRequest {

    private String name;
    private String type;
    private String dimension;
    private String keyTemplate;
    private Integer limit;
    private Integer windowSeconds;

    public void validate() {
        ValidationHelper.validateNotBlank(name, "name");
        ValidationHelper.validateSize(name, 64, "name");
        ValidationHelper.validateSize(type, 32, "type");
        ValidationHelper.validateSize(dimension, 32, "dimension");
        ValidationHelper.validateSize(keyTemplate, 256, "keyTemplate");
        ValidationHelper.validateNotNull(limit, "limit");
        ValidationHelper.validateRange(limit, 1, 1000000, "limit");
        ValidationHelper.validateNotNull(windowSeconds, "windowSeconds");
        ValidationHelper.validateRange(windowSeconds, 1, 3600, "windowSeconds");
    }
}
