package com.apiflow.interfaces.biz.plugin.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginConfigCreateRequest {
    private String pluginCode;
    private String pluginName;
    private String pluginClass;
    private String description;
    private String config;
    private Boolean enabled;
    private Integer orderNum;
}
