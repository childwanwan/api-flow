package com.apiflow.api.repository.plugin.param;

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
public class SelectOnePluginConfigParam {
    private FieldCondition<String> pluginCode;
    private FieldCondition<String> pluginName;
    private List<PluginConfigField> selectFields;
    private List<QueryCondition<PluginConfigField>> conditions;
    private ConditionNode condition;
}
