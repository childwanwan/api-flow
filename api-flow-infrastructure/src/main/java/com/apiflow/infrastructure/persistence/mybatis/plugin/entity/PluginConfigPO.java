package com.apiflow.infrastructure.persistence.mybatis.plugin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("plugin_config")
public class PluginConfigPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("plugin_code")
    private String pluginCode;

    @TableField("plugin_name")
    private String pluginName;

    @TableField("plugin_class")
    private String pluginClass;

    @TableField("description")
    private String description;

    @TableField("config")
    private String config;

    @TableField("enabled")
    private Boolean enabled;

    @TableField("order_num")
    private Integer orderNum;

    @TableField("create_time_ms")
    private Long createTimeMs;

    @TableField("update_time_ms")
    private Long updateTimeMs;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @TableField("version")
    private Integer version;
}
