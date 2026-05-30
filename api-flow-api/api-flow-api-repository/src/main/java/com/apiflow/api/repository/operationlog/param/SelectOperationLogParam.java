package com.apiflow.api.repository.operationlog.param;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.apiflow.common.constant.SystemConstant;
import com.apiflow.common.repository.ConditionNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectOperationLogParam {
    private List<OperationLogField> selectFields;
    private ConditionNode condition;
    private Integer limit;

    public Integer getEffectiveLimit() {
        return ObjectUtil.isNotEmpty(this.limit) ? this.limit : SystemConstant.DEFAULT_MAX_LIMIT;
    }

    public boolean isEmpty() {
        if (CollUtil.isEmpty(selectFields) || ObjectUtil.isEmpty(condition)) {
            return true;
        }
        return false;
    }
}
