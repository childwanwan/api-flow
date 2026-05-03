package com.apigateway.infrastructure.persistence.mybatis.task;

import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.domain.task.query.TaskQuery;
import com.apigateway.domain.task.repository.TaskRepository;
import com.apigateway.infrastructure.persistence.mybatis.task.entity.TaskDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskMapper taskMapper;
    private final TaskConverter taskConverter;

    @Override
    public TaskEntity save(TaskEntity task) {
        TaskDO taskDO = taskConverter.toDO(task);
        taskMapper.insert(taskDO);
        return taskConverter.toEntity(taskDO);
    }

    @Override
    public TaskEntity update(TaskEntity task) {
        TaskDO taskDO = taskConverter.toDO(task);
        taskMapper.updateById(taskDO);
        return taskConverter.toEntity(taskDO);
    }

    @Override
    public TaskEntity query(TaskQuery taskQuery) {
        LambdaQueryWrapper<TaskDO> wrapper = buildQueryWrapper(taskQuery);
        TaskDO taskDO = taskMapper.selectOne(wrapper);
        return taskDO == null ? null : taskConverter.toEntity(taskDO);
    }

    @Override
    public List<TaskEntity> queryList(TaskQuery taskQuery) {
        LambdaQueryWrapper<TaskDO> wrapper = buildQueryWrapper(taskQuery);
        if (taskQuery.getLimit() != null) {
            wrapper.last("LIMIT " + taskQuery.getLimit());
        }
        List<TaskDO> taskDOList = taskMapper.selectList(wrapper);
        return taskDOList.stream()
                .map(taskConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean exists(TaskQuery taskQuery) {
        LambdaQueryWrapper<TaskDO> wrapper = buildQueryWrapper(taskQuery);
        return taskMapper.exists(wrapper);
    }

    private LambdaQueryWrapper<TaskDO> buildQueryWrapper(TaskQuery taskQuery) {
        LambdaQueryWrapper<TaskDO> wrapper = new LambdaQueryWrapper<>();
        if (taskQuery.getTaskNo() != null) {
            wrapper.eq(TaskDO::getTaskNo, taskQuery.getTaskNo());
        }
        if (taskQuery.getApiCode() != null) {
            wrapper.eq(TaskDO::getApiCode, taskQuery.getApiCode());
        }
        if (taskQuery.getStatus() != null) {
            wrapper.eq(TaskDO::getStatus, taskQuery.getStatus());
        }
        if (taskQuery.getSource() != null) {
            wrapper.eq(TaskDO::getSource, taskQuery.getSource());
        }
        return wrapper;
    }

}
