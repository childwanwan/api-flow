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
public class ApiConfigExtraConfigRequest {

    private String key;
    private String value;

    public void validate() {
        if (key == null || key.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "key cannot be blank");
        }
        if (key != null && key.length() > 128) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "key length must be less than or equal to 128");
        }
        if (value != null && value.length() > 2048) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "value length must be less than or equal to 2048");
        }
    }
}