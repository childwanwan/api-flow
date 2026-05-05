package com.flow.api.repository.task;

import com.flow.api.repository.task.idto.TaskIDTO;
import com.flow.api.repository.task.param.SaveTaskParam;
import com.flow.api.repository.task.param.SelectOneTaskParam;
import com.flow.api.repository.task.param.SelectTaskParam;
import com.flow.api.repository.task.param.UpdateTaskParam;

import java.util.List;

public interface TaskRepository {

    TaskIDTO save(SaveTaskParam param);

    TaskIDTO update(UpdateTaskParam param);

    TaskIDTO selectOne(SelectOneTaskParam param);

    List<TaskIDTO> selectList(SelectTaskParam param);

}
