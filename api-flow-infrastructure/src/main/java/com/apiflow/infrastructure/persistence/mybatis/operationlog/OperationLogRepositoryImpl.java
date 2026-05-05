package com.apiflow.infrastructure.persistence.mybatis.operationlog;

import com.apiflow.api.repository.operationlog.OperationLogRepository;
import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.SaveOperationLogParam;
import com.apiflow.api.repository.operationlog.param.SelectOperationLogParam;
import com.apiflow.infrastructure.persistence.mybatis.operationlog.converter.OperationLogConverter;
import com.apiflow.infrastructure.persistence.mybatis.operationlog.entity.OperationLogPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OperationLogRepositoryImpl implements OperationLogRepository {

    private final OperationLogMapper operationLogMapper;

    @Override
    public OperationLogIDTO save(SaveOperationLogParam param) {
        OperationLogPO po = OperationLogConverter.INSTANCE.saveParamToPO(param);
        operationLogMapper.insert(po);
        return OperationLogConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public List<OperationLogIDTO> selectList(SelectOperationLogParam param) {
        LambdaQueryWrapper<OperationLogPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getUsername, param.getUsername());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getOperation, param.getOperation());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getModule, param.getModule());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        wrapper.orderByDesc(OperationLogPO::getCreateTimeMs);
        if (param.getLimit() != null) {
            wrapper.last("LIMIT " + param.getLimit());
        }
        List<OperationLogPO> list = operationLogMapper.selectList(wrapper);
        return list.stream()
                .map(OperationLogConverter.INSTANCE::poToIDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long count(SelectOperationLogParam param) {
        LambdaQueryWrapper<OperationLogPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getUsername, param.getUsername());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getOperation, param.getOperation());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getModule, param.getModule());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        return operationLogMapper.selectCount(wrapper);
    }
}
