package com.apiflow.api.repository.group.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveApiGroupParam {
    private String groupNo;
    private String groupCode;
    private String groupName;
    private String groupDescription;
    private Long createTimeMs;
    private Long updateTimeMs;
    private String createOperator;
    private String updateOperator;
}
