package com.apiflow.api.repository.configlog.param;

import cn.hutool.core.util.ObjectUtil;
import com.apiflow.common.constant.SystemConstant;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.common.repository.QueryCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectConfigChangeLogParam {
    private FieldCondition<String> apiCode;
    private FieldCondition<String> changeType;
    private FieldCondition<Long> createTimeMs;
    private Integer limit;
    private Integer offset;
    private List<ConfigChangeLogField> selectFields;
    private List<QueryCondition<ConfigChangeLogField>> conditions;
    private ConditionNode condition;

    public Integer getEffectiveLimit() {
        return ObjectUtil.isNotEmpty(this.getLimit()) ? this.getLimit() : SystemConstant.DEFAULT_MAX_LIMIT;
    }
}
