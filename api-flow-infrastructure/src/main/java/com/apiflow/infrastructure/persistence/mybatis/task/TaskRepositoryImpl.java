package com.apiflow.infrastructure.persistence.mybatis.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.apiflow.api.repository.task.idto.TaskIDTO;
import com.apiflow.api.repository.task.param.*;
import com.apiflow.api.repository.task.TaskRepository;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.infrastructure.persistence.mybatis.task.converter.TaskConverter;
import com.apiflow.infrastructure.persistence.mybatis.task.entity.TaskPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createFieldResolver;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskMapper taskMapper;

    @Override
    public TaskIDTO findByTaskNo(String taskNo) {
        QueryWrapper<TaskPO> wrapper = new QueryWrapper<>();
        wrapper.eq(TaskField.TASK_NO.getColumnName(), taskNo);
        wrapper.last("LIMIT 1");
        List<TaskPO> list = taskMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return TaskConverter.INSTANCE.taskEntityPOToTaskIDTO(list.get(0));
    }

    @Override
    public void save(SaveTaskParam param) {
        TaskPO taskPO = TaskConverter.INSTANCE.saveTaskParamToTaskEntityPO(param);
        taskMapper.insert(taskPO);
    }

    @Override
    public void update(UpdateTaskParam param) {
        TaskPO taskPO = TaskConverter.INSTANCE.updateTaskParamToTaskEntityPO(param);
        taskMapper.updateById(taskPO);
    }

    @Override
    public TaskIDTO selectOne(SelectOneTaskParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<TaskPO> wrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(param.getSelectFields())) {
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        }
        applyConditions(wrapper, param);
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(TaskField.values()));
        wrapper.orderByDesc("id").last("LIMIT 1");
        List<TaskPO> list = taskMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return TaskConverter.INSTANCE.taskEntityPOToTaskIDTO(list.get(0));
    }

    @Override
    public List<TaskIDTO> selectList(SelectTaskParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<TaskPO> wrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(param.getSelectFields())) {
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        }
        applyConditions(wrapper, param);
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(TaskField.values()));
        wrapper.orderByDesc("id");
        wrapper.last("LIMIT " + param.getEffectiveLimit());
        List<TaskPO> taskPOList = taskMapper.selectList(wrapper);
        return taskPOList.stream()
                .map(TaskConverter.INSTANCE::taskEntityPOToTaskIDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long count(SelectTaskParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<TaskPO> wrapper = new QueryWrapper<>();
        applyConditions(wrapper, param);
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(TaskField.values()));
        return taskMapper.selectCount(wrapper);
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

}
