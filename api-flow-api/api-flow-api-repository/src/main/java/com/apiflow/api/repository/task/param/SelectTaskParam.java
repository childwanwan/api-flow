package com.apiflow.api.repository.task.param;

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
public class SelectTaskParam {
    private FieldCondition<String> taskNo;
    private FieldCondition<String> apiCode;
    private FieldCondition<String> apiName;
    private FieldCondition<String> status;
    private FieldCondition<String> source;
    private FieldCondition<String> actionType;
    private FieldCondition<String> groupNo;
    private FieldCondition<Integer> priority;
    private FieldCondition<Long> createTimeMs;
    private FieldCondition<Long> updateTimeMs;
    private FieldCondition<String> requestContextTraceId;
    private FieldCondition<Integer> execInfoAttemptCount;
    private Integer limit;
    private Integer offset;
    private List<TaskField> selectFields;
    private List<QueryCondition<TaskField>> conditions;
    private ConditionNode condition;

    public Integer getEffectiveLimit() {
        return ObjectUtil.isNotEmpty(this.getLimit()) ? this.getLimit() : SystemConstant.DEFAULT_MAX_LIMIT;
    }
}
