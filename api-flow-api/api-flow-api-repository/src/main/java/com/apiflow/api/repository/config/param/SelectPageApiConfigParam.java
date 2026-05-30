package com.apiflow.api.repository.config.param;

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
public class SelectPageApiConfigParam {
    private Long current;
    private Long size;
    private List<ApiConfigField> selectFields;
    private List<OrderBy> orders;
    private ConditionNode condition;
}
