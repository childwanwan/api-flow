package com.apigateway.infrastructure.persistence.mybatis.config;

import com.apigateway.infrastructure.persistence.mybatis.config.entity.ApiConfigDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiConfigMapper extends BaseMapper<ApiConfigDO> {

}
