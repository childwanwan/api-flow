package com.apigateway.domain.task.repository;

import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.domain.task.query.TaskQuery;

import java.util.List;

public interface TaskRepository {

    TaskEntity save(TaskEntity task);

    TaskEntity update(TaskEntity task);

    TaskEntity query(TaskQuery query);

    List<TaskEntity> queryList(TaskQuery query);

    boolean exists(TaskQuery query);

}
