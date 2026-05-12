package com.apiflow.infrastructure.persistence.mybatis.group.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_group")
public class ApiGroupPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("group_no")
    private String groupNo;

    @TableField("group_code")
    private String groupCode;

    @TableField("group_name")
    private String groupName;

    @TableField("group_description")
    private String groupDescription;

    @TableField("create_time_ms")
    private Long createTimeMs;

    @TableField("update_time_ms")
    private Long updateTimeMs;

    @TableField("create_operator")
    private String createOperator;

    @TableField("update_operator")
    private String updateOperator;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @TableField("version")
    @Version
    private Integer version;
}
