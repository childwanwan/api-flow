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
public class ApiConfigExtraConfigRequest {

    private String key;
    private String value;

    public void validate() {
        ValidationHelper.validateNotBlank(key, "key");
        ValidationHelper.validateSize(key, 128, "key");
        ValidationHelper.validateSize(value, 2048, "value");
    }
}
