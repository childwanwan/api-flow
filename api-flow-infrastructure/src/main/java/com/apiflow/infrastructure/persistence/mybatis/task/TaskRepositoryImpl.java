package com.apiflow.infrastructure.persistence.mybatis.task;

import com.apiflow.api.repository.task.idto.TaskIDTO;
import com.apiflow.api.repository.task.param.*;
import com.apiflow.api.repository.task.TaskRepository;
import com.apiflow.infrastructure.persistence.mybatis.task.converter.TaskConverter;
import com.apiflow.infrastructure.persistence.mybatis.task.entity.TaskPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
        if (param.getSelectFields() != null && !param.getSelectFields().isEmpty()) {
            QueryWrapper<TaskPO> wrapper = new QueryWrapper<>();
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
            applyConditions(wrapper, param);
            QueryConditionHelper.applyConditions(wrapper, param.getConditions());
            TaskPO taskDO = taskMapper.selectOne(wrapper);
            return taskDO == null ? null : TaskConverter.INSTANCE.taskEntityPOToTaskIDTO(taskDO);
        }
        LambdaQueryWrapper<TaskPO> wrapper = buildLambdaQueryWrapper(param);
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        TaskPO taskDO = taskMapper.selectOne(wrapper);
        return taskDO == null ? null : TaskConverter.INSTANCE.taskEntityPOToTaskIDTO(taskDO);
    }

    @Override
    public List<TaskIDTO> selectList(SelectTaskParam param) {
        if (param.getSelectFields() != null && !param.getSelectFields().isEmpty()) {
            QueryWrapper<TaskPO> wrapper = new QueryWrapper<>();
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
            applyConditions(wrapper, param);
            QueryConditionHelper.applyConditions(wrapper, param.getConditions());
            if (param.getLimit() != null) {
                wrapper.last(param.getLimit() > 0, "LIMIT " + param.getLimit());
            }
            List<TaskPO> taskPOList = taskMapper.selectList(wrapper);
            return taskPOList.stream()
                    .map(TaskConverter.INSTANCE::taskEntityPOToTaskIDTO)
                    .collect(Collectors.toList());
        }
        LambdaQueryWrapper<TaskPO> wrapper = buildLambdaQueryWrapper(param);
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        if (param.getLimit() != null) {
            wrapper.last(param.getLimit() > 0, "LIMIT " + param.getLimit());
        }
        List<TaskPO> taskPOList = taskMapper.selectList(wrapper);
        return taskPOList.stream()
                .map(TaskConverter.INSTANCE::taskEntityPOToTaskIDTO)
                .collect(Collectors.toList());
    }

    private void applyConditions(QueryWrapper<TaskPO> wrapper, SelectOneTaskParam param) {
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.TASK_NO.getColumnName(), param.getTaskNo());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.API_CODE.getColumnName(), param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.API_NAME.getColumnName(), param.getApiName());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.STATUS.getColumnName(), param.getStatus());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.SOURCE.getColumnName(), param.getSource());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.ACTION_TYPE.getColumnName(), param.getActionType());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.GROUP_NO.getColumnName(), param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.PRIORITY.getColumnName(), param.getPriority());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.CREATE_TIME_MS.getColumnName(), param.getCreateTimeMs());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.UPDATE_TIME_MS.getColumnName(), param.getUpdateTimeMs());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "request_context", "traceId", param.getRequestContextTraceId());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "exec_info", "attemptCount", param.getExecInfoAttemptCount());
    }

    private void applyConditions(QueryWrapper<TaskPO> wrapper, SelectTaskParam param) {
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.TASK_NO.getColumnName(), param.getTaskNo());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.API_CODE.getColumnName(), param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.API_NAME.getColumnName(), param.getApiName());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.STATUS.getColumnName(), param.getStatus());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.SOURCE.getColumnName(), param.getSource());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.ACTION_TYPE.getColumnName(), param.getActionType());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.GROUP_NO.getColumnName(), param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.PRIORITY.getColumnName(), param.getPriority());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.CREATE_TIME_MS.getColumnName(), param.getCreateTimeMs());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskField.UPDATE_TIME_MS.getColumnName(), param.getUpdateTimeMs());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "request_context", "traceId", param.getRequestContextTraceId());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "exec_info", "attemptCount", param.getExecInfoAttemptCount());
    }

    private LambdaQueryWrapper<TaskPO> buildLambdaQueryWrapper(SelectOneTaskParam param) {
        LambdaQueryWrapper<TaskPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getTaskNo, param.getTaskNo());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getApiCode, param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getApiName, param.getApiName());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getStatus, param.getStatus());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getSource, param.getSource());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getActionType, param.getActionType());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getGroupNo, param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getPriority, param.getPriority());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getUpdateTimeMs, param.getUpdateTimeMs());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "request_context", "traceId", param.getRequestContextTraceId());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "exec_info", "attemptCount", param.getExecInfoAttemptCount());
        return wrapper;
    }

    private LambdaQueryWrapper<TaskPO> buildLambdaQueryWrapper(SelectTaskParam param) {
        LambdaQueryWrapper<TaskPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getTaskNo, param.getTaskNo());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getApiCode, param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getApiName, param.getApiName());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getStatus, param.getStatus());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getSource, param.getSource());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getActionType, param.getActionType());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getGroupNo, param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getPriority, param.getPriority());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskPO::getUpdateTimeMs, param.getUpdateTimeMs());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "request_context", "traceId", param.getRequestContextTraceId());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "exec_info", "attemptCount", param.getExecInfoAttemptCount());
        return wrapper;
    }

}
