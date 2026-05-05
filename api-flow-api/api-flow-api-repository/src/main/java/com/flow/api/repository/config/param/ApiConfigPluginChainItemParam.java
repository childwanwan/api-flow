package com.flow.api.repository.config.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigPluginChainItemParam {
    private String pluginCode;
    private Integer order;
    private Boolean enabled;
    private Object config;
}
