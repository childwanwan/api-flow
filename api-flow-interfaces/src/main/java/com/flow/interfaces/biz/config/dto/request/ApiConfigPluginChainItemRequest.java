package com.flow.interfaces.biz.config.dto.request;

import com.flow.common.exception.BusinessException;
import com.flow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigPluginChainItemRequest {

    private String pluginName;
    private Integer order;
    private String config;

    public void validate() {
        if (pluginName == null || pluginName.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "pluginName cannot be blank");
        }
        if (pluginName != null && pluginName.length() > 64) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "pluginName length must be less than or equal to 64");
        }
        if (order == null) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "order cannot be null");
        }
        if (order < 1 || order > 100) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "order must be between 1 and 100");
        }
        if (config != null && config.length() > 2048) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "config length must be less than or equal to 2048");
        }
    }
}