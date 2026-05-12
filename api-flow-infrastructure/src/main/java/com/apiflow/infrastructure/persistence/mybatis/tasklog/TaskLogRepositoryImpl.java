package com.apiflow.infrastructure.persistence.mybatis.tasklog;

import com.apiflow.api.repository.tasklog.TaskLogRepository;
import com.apiflow.api.repository.tasklog.idto.TaskLogIDTO;
import com.apiflow.api.repository.tasklog.param.SaveTaskLogParam;
import com.apiflow.api.repository.tasklog.param.SelectTaskLogParam;
import com.apiflow.api.repository.tasklog.param.TaskLogField;
import com.apiflow.infrastructure.persistence.mybatis.tasklog.converter.TaskLogConverter;
import com.apiflow.infrastructure.persistence.mybatis.tasklog.entity.TaskLogPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createFieldResolver;

@Repository
@RequiredArgsConstructor
public class TaskLogRepositoryImpl implements TaskLogRepository {

    private final TaskLogMapper taskLogMapper;

    @Override
    public TaskLogIDTO save(SaveTaskLogParam param) {
        TaskLogPO po = TaskLogConverter.INSTANCE.saveParamToPO(param);
        taskLogMapper.insert(po);
        return TaskLogConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public List<TaskLogIDTO> selectList(SelectTaskLogParam param) {
        LambdaQueryWrapper<TaskLogPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, TaskLogPO::getTaskNo, param.getTaskNo());
        QueryConditionHelper.applyFieldCondition(wrapper, TaskLogPO::getLogType, param.getLogType());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(TaskLogField.values()));
        wrapper.orderByDesc(TaskLogPO::getCreateTimeMs);
        if (param.getLimit() != null) {
            wrapper.last("LIMIT " + param.getLimit());
        }
        List<TaskLogPO> list = taskLogMapper.selectList(wrapper);
        return list.stream()
                .map(TaskLogConverter.INSTANCE::poToIDTO)
                .collect(Collectors.toList());
    }
}
