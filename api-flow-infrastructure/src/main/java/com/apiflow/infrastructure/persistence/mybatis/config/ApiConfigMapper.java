package com.apiflow.infrastructure.persistence.mybatis.config;

import com.apiflow.infrastructure.persistence.mybatis.config.entity.ApiConfigPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiConfigMapper extends BaseMapper<ApiConfigPO> {

}
