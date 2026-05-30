package com.apiflow.api.repository.config.param;

import cn.hutool.core.util.ObjectUtil;
import com.apiflow.common.constant.SystemConstant;
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
public class SelectApiConfigParam {
    private Integer limit;
    private List<ApiConfigField> selectFields;
    private List<OrderBy> orders;
    private ConditionNode condition;

    public Integer getEffectiveLimit() {
        return ObjectUtil.isNotEmpty(this.getLimit()) ? this.getLimit() : SystemConstant.DEFAULT_MAX_LIMIT;
    }
}
