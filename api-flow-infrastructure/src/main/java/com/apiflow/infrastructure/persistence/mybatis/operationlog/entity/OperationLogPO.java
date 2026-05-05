package com.apiflow.infrastructure.persistence.mybatis.operationlog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_operation_log")
public class OperationLogPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("operation")
    private String operation;

    @TableField("module")
    private String module;

    @TableField("detail")
    private String detail;

    @TableField("ip")
    private String ip;

    @TableField("create_time_ms")
    private Long createTimeMs;
}
