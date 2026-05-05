package com.apiflow.interfaces.biz.config.request;

import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigPluginConfigRequest {

    private Boolean enabled;
    private List<ApiConfigPluginChainItemRequest> pluginChain;

    public void validate() {
        if (pluginChain != null && pluginChain.size() > 10) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "plugin chain items count must be less than or equal to 10");
        }
        if (pluginChain != null) {
            for (int i = 0; i < pluginChain.size(); i++) {
                pluginChain.get(i).validate();
            }
        }
    }
}
