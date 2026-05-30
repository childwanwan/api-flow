package com.apiflow.api.repository.operationlog.param;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
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
public class SelectOneOperationLogParam {
    private List<OperationLogField> selectFields;
    private ConditionNode condition;

    public boolean isEmpty() {
        if (CollUtil.isEmpty(selectFields) || ObjectUtil.isEmpty(condition)) {
            return true;
        }
        return false;
    }
}
