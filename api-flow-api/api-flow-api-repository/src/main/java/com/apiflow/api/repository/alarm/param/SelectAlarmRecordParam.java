package com.apiflow.api.repository.alarm.param;

import cn.hutool.core.util.ObjectUtil;
import com.apiflow.common.constant.SystemConstant;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.repository.FieldCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectAlarmRecordParam {
    private FieldCondition<String> eventType;
    private FieldCondition<String> level;
    private FieldCondition<Long> createTimeMs;
    private Integer limit;
    private ConditionNode condition;

    public Integer getEffectiveLimit() {
        return ObjectUtil.isNotEmpty(this.getLimit()) ? this.getLimit() : SystemConstant.DEFAULT_MAX_LIMIT;
    }
}
