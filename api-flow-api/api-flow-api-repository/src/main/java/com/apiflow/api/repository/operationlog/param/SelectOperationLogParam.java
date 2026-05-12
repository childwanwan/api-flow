package com.apiflow.api.repository.operationlog.param;

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
public class SelectOperationLogParam {
    private FieldCondition<String> bizCode;
    private FieldCondition<String> logType;
    private FieldCondition<String> operator;
    private FieldCondition<Long> operateTimeMs;
    private FieldCondition<Long> createTimeMs;
    private Integer limit;
    private Integer offset;
    private List<OperationLogField> selectFields;
    private List<QueryCondition<OperationLogField>> conditions;
    private ConditionNode condition;
}
