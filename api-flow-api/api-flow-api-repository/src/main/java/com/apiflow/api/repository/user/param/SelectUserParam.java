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
public class SelectUserParam {

    private FieldCondition<String> username;
    private FieldCondition<String> role;
    private FieldCondition<String> status;
    private List<UserField> selectFields;
    private List<QueryCondition<UserField>> conditions;

    public boolean isEmpty() {
        return !hasAnyFieldCondition()
                && (conditions == null || conditions.isEmpty());
    }

    private boolean hasAnyFieldCondition() {
        return (username != null && username.hasAnyCondition())
                || (role != null && role.hasAnyCondition())
                || (status != null && status.hasAnyCondition());
    }

}
