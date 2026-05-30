package com.apiflow.infrastructure.persistence.mybatis.operationlog;

import com.apiflow.api.repository.operationlog.OperationLogRepository;
import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.*;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
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
    public void save(SaveOperationLogParam param) {
        OperationLogPO po = OperationLogConverter.INSTANCE.saveParamToPO(param);
        operationLogMapper.insert(po);
    }

    @Override
    public OperationLogIDTO selectOne(SelectOneOperationLogParam param) {
        if (ObjectUtil.isEmpty(param) || param.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<OperationLogPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        applyCondition(wrapper, param.getCondition());
        wrapper.orderByDesc(OperationLogField.ID.getColumnName()).last("LIMIT 1");
        List<OperationLogPO> list = operationLogMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return OperationLogConverter.INSTANCE.poToIDTO(list.get(0));
    }

    @Override
    public List<OperationLogIDTO> selectList(SelectOperationLogParam param) {
        if (ObjectUtil.isEmpty(param) || CollUtil.isEmpty(param.getSelectFields())) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<OperationLogPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        applyCondition(wrapper, param.getCondition());
        wrapper.orderByDesc(OperationLogField.CREATE_TIME_MS.getColumnName());
        wrapper.last("LIMIT " + param.getEffectiveLimit());
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
        if (ObjectUtil.isEmpty(param) || CollUtil.isEmpty(param.getSelectFields())) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
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
        if (CollUtil.isEmpty(orders)) {
            wrapper.orderByDesc(OperationLogField.CREATE_TIME_MS.getColumnName()).orderByDesc(OperationLogField.ID.getColumnName());
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
