package com.apiflow.api.repository.config.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigPluginConfigIDTO {
    private Boolean enabled;
    private List<ApiConfigPluginChainItemIDTO> pluginChain;
}
