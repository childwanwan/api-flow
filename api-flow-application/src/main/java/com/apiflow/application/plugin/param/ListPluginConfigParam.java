package com.apiflow.application.plugin.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListPluginConfigParam {
    private String pluginCode;
    private String pluginName;
}
