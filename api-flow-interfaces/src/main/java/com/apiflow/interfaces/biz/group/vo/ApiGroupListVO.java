package com.apiflow.interfaces.biz.group.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiGroupListVO {

    private String groupNo;
    private String groupCode;

    private String groupName;
}
