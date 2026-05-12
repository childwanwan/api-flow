package com.apiflow.api.repository.configlog.param;

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
}
