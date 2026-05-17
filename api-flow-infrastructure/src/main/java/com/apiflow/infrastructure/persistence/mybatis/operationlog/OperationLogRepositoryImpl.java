package com.apiflow.infrastructure.persistence.mybatis.operationlog;

import com.apiflow.api.repository.operationlog.OperationLogRepository;
import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.*;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.result.PageResult;
import com.apiflow.infrastructure.persistence.mybatis.operationlog.converter.OperationLogConverter;
import com.apiflow.infrastructure.persistence.mybatis.operationlog.entity.OperationLogPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createFieldResolver;

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
        QueryWrapper<OperationLogPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        applyCondition(wrapper, param.getCondition());
        wrapper.orderByDesc("create_time_ms");
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
        QueryWrapper<OperationLogPO> wrapper = new QueryWrapper<>();
        applyCondition(wrapper, param.getCondition());
        return operationLogMapper.selectCount(wrapper);
    }

    @Override
    public PageResult<OperationLogIDTO> selectPage(SelectPageOperationLogParam param) {
        QueryWrapper<OperationLogPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        applyCondition(wrapper, param.getCondition());
        applySorting(wrapper, param);

        Page<OperationLogPO> page = new Page<>(param.getCurrent(), param.getSize());
        IPage<OperationLogPO> result = operationLogMapper.selectPage(page, wrapper);
        return OperationLogConverter.INSTANCE.operationLogPOIPage2PageResult(result);
    }

    private void applyCondition(QueryWrapper<OperationLogPO> wrapper, ConditionNode condition) {
        QueryConditionHelper.applyConditionNode(wrapper, condition, createFieldResolver(OperationLogField.values()));
    }

    private void applySorting(QueryWrapper<OperationLogPO> wrapper, SelectPageOperationLogParam param) {
        List<OrderBy> orders = param.getOrders();
        if (orders == null || orders.isEmpty()) {
            wrapper.orderByDesc("create_time_ms").orderByDesc("id");
            return;
        }

        List<OrderBy> sortedOrders = orders.stream()
                .filter(order -> order.getField() != null)
                .sorted(Comparator.comparing(order -> order.getOrder() != null ? order.getOrder() : Integer.MAX_VALUE))
                .collect(Collectors.toList());

        for (OrderBy order : sortedOrders) {
            boolean isAsc = Boolean.TRUE.equals(order.getAscending());
            String column = order.getField().getColumnName();
            wrapper.orderBy(true, isAsc, column);
        }
    }
}
