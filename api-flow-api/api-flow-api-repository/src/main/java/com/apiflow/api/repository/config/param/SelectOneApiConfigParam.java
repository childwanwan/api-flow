package com.apiflow.api.repository.config.param;

import cn.hutool.core.collection.CollectionUtil;
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
public class SelectOneApiConfigParam {
    private List<ApiConfigField> selectFields;
    private ConditionNode condition;

    public boolean isEmpty() {
        if (CollectionUtil.isEmpty(selectFields)
                || ObjectUtil.isEmpty(condition)) {
            return true;
        }
        return false;
    }
}
