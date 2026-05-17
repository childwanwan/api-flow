package com.apiflow.interfaces.biz.group.request;

import cn.hutool.core.util.StrUtil;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiGroupCreateRequest {

    private String groupCode;

    private String groupName;

    private String groupDescription;


    public void validate() {
        if (StrUtil.isBlank(this.getGroupCode())) {
            throw new BusinessException(ErrorCode.GROUP_CODE_EMPTY);
        }
        if (this.getGroupCode().length() > 64) {
            throw new BusinessException(ErrorCode.GROUP_CODE_TOO_LONG);
        }
        if (StrUtil.isBlank(this.getGroupName())) {
            throw new BusinessException(ErrorCode.GROUP_NAME_EMPTY);
        }
        if (this.getGroupName().length() > 128) {
            throw new BusinessException(ErrorCode.GROUP_NAME_TOO_LONG);
        }
        if (this.getGroupDescription() != null && this.getGroupDescription().length() > 512) {
            throw new BusinessException(ErrorCode.GROUP_DESCRIPTION_TOO_LONG);
        }
    }
}