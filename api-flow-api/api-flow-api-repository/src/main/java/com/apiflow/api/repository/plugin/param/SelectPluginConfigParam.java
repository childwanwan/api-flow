package com.apiflow.api.repository.plugin.param;

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
public class SelectPluginConfigParam {
    private FieldCondition<String> pluginCode;
    private FieldCondition<String> pluginName;
    private FieldCondition<Boolean> enabled;
    private Integer limit;
    private Integer offset;
    private List<PluginConfigField> selectFields;
    private List<QueryCondition<PluginConfigField>> conditions;
}
