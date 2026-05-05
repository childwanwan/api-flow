package com.flow.infrastructure.persistence.mybatis.task;

import com.flow.api.repository.task.idto.TaskIDTO;
import com.flow.api.repository.task.param.*;
import com.flow.api.repository.task.TaskRepository;
import com.flow.infrastructure.persistence.mybatis.task.converter.TaskConverter;
import com.flow.infrastructure.persistence.mybatis.task.entity.TaskPO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskMapper taskMapper;

    @Override
    public TaskIDTO save(SaveTaskParam param) {
        TaskPO taskPO = TaskConverter.INSTANCE.saveTaskParamToTaskEntityPO(param);
        taskMapper.insert(taskPO);
        return TaskConverter.INSTANCE.taskEntityPOToTaskIDTO(taskPO);
    }

    @Override
    public TaskIDTO update(UpdateTaskParam param) {
        TaskPO taskPO = TaskConverter.INSTANCE.updateTaskParamToTaskEntityPO(param);
        taskMapper.updateById(taskPO);
        return TaskConverter.INSTANCE.taskEntityPOToTaskIDTO(taskPO);
    }

    @Override
    public TaskIDTO selectOne(SelectOneTaskParam param) {
        QueryWrapper<TaskPO> wrapper = buildQueryWrapper(param);
        applySelectFields(wrapper, param.getSelectFields());
        TaskPO taskDO = taskMapper.selectOne(wrapper);
        return taskDO == null ? null : TaskConverter.INSTANCE.taskEntityPOToTaskIDTO(taskDO);
    }

    @Override
    public List<TaskIDTO> selectList(SelectTaskParam param) {
        QueryWrapper<TaskPO> wrapper = buildQueryWrapper(param);
        applySelectFields(wrapper, param.getSelectFields());
        if (param.getLimit() != null) {
            wrapper.last("LIMIT " + param.getLimit());
        }
        List<TaskPO> taskPOList = taskMapper.selectList(wrapper);
        return taskPOList.stream()
                .map(TaskConverter.INSTANCE::taskEntityPOToTaskIDTO)
                .collect(Collectors.toList());
    }

    private void applySelectFields(QueryWrapper<TaskPO> wrapper, List<TaskField> selectFields) {
        if (selectFields != null && !selectFields.isEmpty()) {
            List<String> columns = new ArrayList<>();
            for (TaskField field : selectFields) {
                if (field.isJsonField()) {
                    String alias = field.getFieldName().replace(".", "_");
                    columns.add(String.format("JSON_UNQUOTE(JSON_EXTRACT(%s, '$.%s')) AS %s",
                            field.getColumnName(), field.getJsonPath(), alias));
                } else {
                    columns.add(field.getColumnName());
                }
            }
            wrapper.select(columns.toArray(new String[0]));
        }
    }

    private QueryWrapper<TaskPO> buildQueryWrapper(SelectOneTaskParam param) {
        QueryWrapper<TaskPO> wrapper = new QueryWrapper<>();
        if (param.getTaskNo() != null) {
            wrapper.eq("task_no", param.getTaskNo());
        }
        if (param.getApiCode() != null) {
            wrapper.eq("api_code", param.getApiCode());
        }
        if (param.getStatus() != null) {
            wrapper.eq("status", param.getStatus());
        }
        if (param.getSource() != null) {
            wrapper.eq("source", param.getSource());
        }
        return wrapper;
    }

    private QueryWrapper<TaskPO> buildQueryWrapper(SelectTaskParam param) {
        QueryWrapper<TaskPO> wrapper = new QueryWrapper<>();
        if (param.getTaskNo() != null) {
            wrapper.eq("task_no", param.getTaskNo());
        }
        if (param.getApiCode() != null) {
            wrapper.eq("api_code", param.getApiCode());
        }
        if (param.getStatus() != null) {
            wrapper.eq("status", param.getStatus());
        }
        if (param.getSource() != null) {
            wrapper.eq("source", param.getSource());
        }
        return wrapper;
    }

}