package com.apiflow.api.repository.group.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiGroupIDTO {
    private Long id;
    private String groupNo;
    private String groupCode;
    private String groupName;
    private String groupDescription;
    private Long createTimeMs;
    private Long updateTimeMs;
    private String createOperator;
    private String updateOperator;
    private Integer version;
}
