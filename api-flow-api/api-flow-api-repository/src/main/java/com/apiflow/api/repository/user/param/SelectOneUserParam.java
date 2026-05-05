package com.apiflow.api.repository.user.param;

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
public class SelectOneUserParam {
    private FieldCondition<String> username;
    private List<UserField> selectFields;
    private List<QueryCondition<UserField>> conditions;

    public boolean isEmpty() {
        return !(username != null && username.hasAnyCondition())
                && (conditions == null || conditions.isEmpty());
    }
}
