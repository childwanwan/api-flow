package com.apiflow.api.repository.group.param;

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
public class SelectOneApiGroupParam {
    private FieldCondition<String> groupNo;
    private FieldCondition<String> groupName;
    private List<ApiGroupField> selectFields;
    private List<QueryCondition<ApiGroupField>> conditions;
}
