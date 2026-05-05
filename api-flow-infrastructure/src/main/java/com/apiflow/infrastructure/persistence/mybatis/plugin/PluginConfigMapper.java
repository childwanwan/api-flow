package com.apiflow.infrastructure.persistence.mybatis.plugin;

import com.apiflow.infrastructure.persistence.mybatis.plugin.entity.PluginConfigPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PluginConfigMapper extends BaseMapper<PluginConfigPO> {
}
