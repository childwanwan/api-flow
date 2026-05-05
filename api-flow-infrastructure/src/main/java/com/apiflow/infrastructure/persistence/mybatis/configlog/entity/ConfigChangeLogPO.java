package com.apiflow.infrastructure.persistence.mybatis.configlog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_config_log")
public class ConfigChangeLogPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("api_code")
    private String apiCode;

    @TableField("change_type")
    private String changeType;

    @TableField("before_config")
    private String beforeConfig;

    @TableField("after_config")
    private String afterConfig;

    @TableField("operator")
    private String operator;

    @TableField("create_time_ms")
    private Long createTimeMs;
}
