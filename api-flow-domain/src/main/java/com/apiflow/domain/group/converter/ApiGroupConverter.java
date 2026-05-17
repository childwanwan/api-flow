package com.apiflow.domain.group.converter;

import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.domain.group.model.ApiGroup;

public class ApiGroupConverter {

    public static final ApiGroupConverter INSTANCE = new ApiGroupConverter();

    private ApiGroupConverter() {
    }

    public ApiGroup toAggregate(ApiGroupIDTO idto) {
        if (idto == null) {
            return null;
        }
        return ApiGroup.reconstitute()
                .id(idto.getId())
                .groupNo(idto.getGroupNo())
                .groupCode(idto.getGroupCode())
                .groupName(idto.getGroupName())
                .groupDescription(idto.getGroupDescription())
                .createTimeMs(idto.getCreateTimeMs())
                .updateTimeMs(idto.getUpdateTimeMs())
                .createOperator(idto.getCreateOperator())
                .updateOperator(idto.getUpdateOperator())
                .build();
    }
}
