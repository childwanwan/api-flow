package com.apiflow.infrastructure.persistence.mybatis.user;

import com.apiflow.infrastructure.persistence.mybatis.user.entity.UserPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

}