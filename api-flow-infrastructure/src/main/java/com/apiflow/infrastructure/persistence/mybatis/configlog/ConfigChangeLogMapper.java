package com.apiflow.infrastructure.persistence.mybatis.configlog;

import com.apiflow.infrastructure.persistence.mybatis.configlog.entity.ConfigChangeLogPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConfigChangeLogMapper extends BaseMapper<ConfigChangeLogPO> {
}
