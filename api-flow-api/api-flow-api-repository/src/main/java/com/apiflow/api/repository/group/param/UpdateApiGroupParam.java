package com.apiflow.api.repository.group.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApiGroupParam {
    private Long id;
    private String groupNo;
    private String groupCode;
    private String groupName;
    private String groupDescription;
    private Long updateTimeMs;
    private String updateOperator;
}
