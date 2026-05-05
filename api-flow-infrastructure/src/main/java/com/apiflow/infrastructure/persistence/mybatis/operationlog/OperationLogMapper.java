package com.apiflow.infrastructure.persistence.mybatis.operationlog;

import com.apiflow.infrastructure.persistence.mybatis.operationlog.entity.OperationLogPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLogPO> {
}
