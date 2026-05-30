package com.apiflow.interfaces.biz.group.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiGroupVO {
    
    private String groupNo;

    private String groupCode;
    
    private String groupName;
    
    private String groupDescription;

    private Long createTimeMs;
    
    private Long updateTimeMs;

    private Integer apiCount;

}
