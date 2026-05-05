package com.apiflow.api.repository.config.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigPluginChainItemIDTO {
    private String pluginCode;
    private Integer order;
    private Boolean enabled;
    private Object config;
}
