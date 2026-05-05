package com.apiflow.infrastructure.persistence.mybatis.group;

import com.apiflow.infrastructure.persistence.mybatis.group.entity.ApiGroupPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiGroupMapper extends BaseMapper<ApiGroupPO> {
}
