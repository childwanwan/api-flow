package com.apiflow.api.repository.config.param;

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
public class SelectOneApiConfigParam {
    private FieldCondition<String> apiCode;
    private FieldCondition<String> groupNo;
    private FieldCondition<String> status;
    private FieldCondition<String> apiName;
    private FieldCondition<Long> requestTimeoutMs;
    private FieldCondition<Integer> autoRetryCount;
    private FieldCondition<Integer> maxQueueSize;
    private FieldCondition<Long> createTimeMs;
    private FieldCondition<Long> updateTimeMs;
    private FieldCondition<Integer> rateLimitConfigLimit;
    private FieldCondition<Integer> rateLimitConfigWindowSeconds;
    private List<ApiConfigField> selectFields;
    private List<QueryCondition<ApiConfigField>> conditions;
    private ConditionNode condition;
}
