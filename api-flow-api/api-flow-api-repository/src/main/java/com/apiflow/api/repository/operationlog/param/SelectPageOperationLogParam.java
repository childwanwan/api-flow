package com.apiflow.api.repository.operationlog.param;

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
public class SelectPageOperationLogParam {

    private Long current;

    private Long size;

    private List<OperationLogField> selectFields;

    private List<OrderBy> orders;

    private ConditionNode condition;
}
