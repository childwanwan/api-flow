package com.apiflow.api.repository.task;

import com.apiflow.api.repository.task.idto.TaskIDTO;
import com.apiflow.api.repository.task.param.SaveTaskParam;
import com.apiflow.api.repository.task.param.SelectOneTaskParam;
import com.apiflow.api.repository.task.param.SelectTaskParam;
import com.apiflow.api.repository.task.param.UpdateTaskParam;

import java.util.List;

public interface TaskRepository {

    TaskIDTO findByTaskNo(String taskNo);

    void save(SaveTaskParam param);

    void update(UpdateTaskParam param);

    TaskIDTO selectOne(SelectOneTaskParam param);

    List<TaskIDTO> selectList(SelectTaskParam param);

    long count(SelectTaskParam param);

}
