package com.apiflow.domain.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginChainItem {

    private String pluginCode;
    private Integer order;
    private Boolean enabled;
    private String config;
}
