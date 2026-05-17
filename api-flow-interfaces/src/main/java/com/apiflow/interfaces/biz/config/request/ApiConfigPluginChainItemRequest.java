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
public class ApiConfigPluginChainItemRequest {

    private String pluginCode;
    private Boolean enabled;
    private Integer order;
    private String config;

    public void validate() {
        ValidationHelper.validateNotBlank(pluginCode, "pluginCode");
        ValidationHelper.validateSize(pluginCode, 64, "pluginCode");
        ValidationHelper.validateNotNull(order, "order");
        ValidationHelper.validateRange(order, 1, 100, "order");
        ValidationHelper.validateSize(config, 2048, "config");
    }
}
