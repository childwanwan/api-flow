package com.apiflow.application.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginChainItemDTO {

    private String pluginCode;
    private Integer order;
    private Boolean enabled;
    private String config;
}
