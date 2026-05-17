package com.apiflow.api.repository.group.param;

import cn.hutool.core.collection.CollectionUtil;
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
public class SelectPageApiGroupParam {
    private Long current;

    private Long size;

    private List<ApiGroupField> selectFields;

    private List<OrderBy> orders;
    private ConditionNode condition;
}
