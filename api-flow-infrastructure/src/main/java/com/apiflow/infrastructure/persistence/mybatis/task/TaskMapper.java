package com.apiflow.infrastructure.persistence.mybatis.task;

import com.apiflow.infrastructure.persistence.mybatis.task.entity.TaskPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskMapper extends BaseMapper<TaskPO> {
}
