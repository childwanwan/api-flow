package com.apiflow.api.repository.plugin.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavePluginConfigParam {
    private String pluginCode;
    private String pluginName;
    private String pluginClass;
    private String description;
    private String config;
    private Boolean enabled;
    private Integer orderNum;
    private Long createTimeMs;
    private Long updateTimeMs;
}
