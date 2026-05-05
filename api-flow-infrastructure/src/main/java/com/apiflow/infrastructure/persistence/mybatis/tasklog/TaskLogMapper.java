package com.apiflow.infrastructure.persistence.mybatis.tasklog;

import com.apiflow.infrastructure.persistence.mybatis.tasklog.entity.TaskLogPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskLogMapper extends BaseMapper<TaskLogPO> {
}
