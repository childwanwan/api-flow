package com.apiflow.interfaces.biz.group.request;

import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiGroupUpdateRequest {

    private String groupNo;

    private String groupCode;

    private String groupName;

    private String groupDescription;

    public void validate() {
        if (StringUtils.isBlank(this.getGroupNo())) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED);
        }
    }
}