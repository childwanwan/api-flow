package com.apiflow.api.repository.tasklog.param;

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
public class SelectTaskLogParam {
    private FieldCondition<String> taskNo;
    private FieldCondition<String> logType;
    private Integer limit;
    private Integer offset;
    private List<TaskLogField> selectFields;
    private List<QueryCondition<TaskLogField>> conditions;
}
