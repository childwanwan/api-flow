package com.flow.interfaces.biz.config.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigPluginChainItemVO {
    private String pluginName;
    private Integer order;
    private String config;
}
