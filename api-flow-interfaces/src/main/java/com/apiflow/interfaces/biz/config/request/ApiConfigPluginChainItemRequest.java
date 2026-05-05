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

    private String pluginName;
    private Integer order;
    private String config;

    public void validate() {
        ValidationHelper.validateNotBlank(pluginName, "pluginName");
        ValidationHelper.validateSize(pluginName, 64, "pluginName");
        ValidationHelper.validateNotNull(order, "order");
        ValidationHelper.validateRange(order, 1, 100, "order");
        ValidationHelper.validateSize(config, 2048, "config");
    }
}
