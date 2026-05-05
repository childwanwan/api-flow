package com.apiflow.api.repository.tasklog;

import com.apiflow.api.repository.tasklog.idto.TaskLogIDTO;
import com.apiflow.api.repository.tasklog.param.SaveTaskLogParam;
import com.apiflow.api.repository.tasklog.param.SelectTaskLogParam;

import java.util.List;

public interface TaskLogRepository {

    TaskLogIDTO save(SaveTaskLogParam param);

    List<TaskLogIDTO> selectList(SelectTaskLogParam param);
}
