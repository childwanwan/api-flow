package com.apigateway.infrastructure.persistence.mybatis.task;

import com.apigateway.infrastructure.persistence.mybatis.task.entity.TaskDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<TaskDO> {

    @Select("SELECT * FROM api_task WHERE status = 'PENDING' AND interrupt_flag = 0 AND deleted = 0 ORDER BY priority ASC, create_time_ms ASC LIMIT #{limit}")
    List<TaskDO> selectPendingTasks(@Param("limit") int limit);

}
