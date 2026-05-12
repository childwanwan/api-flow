package com.apiflow.infrastructure.persistence.mybatis.operationlog;

import com.apiflow.api.repository.operationlog.OperationLogRepository;
import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.OrderBy;
import com.apiflow.api.repository.operationlog.param.SaveOperationLogParam;
import com.apiflow.api.repository.operationlog.param.SelectOperationLogParam;
import com.apiflow.api.repository.operationlog.param.SelectPageOperationLogParam;
import com.apiflow.api.repository.operationlog.param.OperationLogField;
import com.apiflow.common.result.PageResult;
import com.apiflow.infrastructure.persistence.mybatis.operationlog.converter.OperationLogConverter;
import com.apiflow.infrastructure.persistence.mybatis.operationlog.entity.OperationLogPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        LambdaQueryWrapper<OperationLogPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getBizCode, param.getBizCode());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getLogType, param.getLogType());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getOperator, param.getOperator());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getOperateTimeMs, param.getOperateTimeMs());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(OperationLogField.values()));
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
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getBizCode, param.getBizCode());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getLogType, param.getLogType());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getOperator, param.getOperator());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getOperateTimeMs, param.getOperateTimeMs());
        QueryConditionHelper.applyFieldCondition(wrapper, OperationLogPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(OperationLogField.values()));
        return operationLogMapper.selectCount(wrapper);
    }

    @Override
    public PageResult<OperationLogIDTO> selectPage(SelectPageOperationLogParam param) {
        LambdaQueryWrapper<OperationLogPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(OperationLogField.values()));
        applySorting(wrapper, param);

        Page<OperationLogPO> page = new Page<>(param.getCurrent(), param.getSize());
        IPage<OperationLogPO> result = operationLogMapper.selectPage(page, wrapper);
        return OperationLogConverter.INSTANCE.operationLogPOIPage2PageResult(result);
    }

    private void applySorting(LambdaQueryWrapper<OperationLogPO> wrapper, SelectPageOperationLogParam param) {
        List<OrderBy> orders = param.getOrders();
        if (orders == null || orders.isEmpty()) {
            wrapper.orderByDesc(OperationLogPO::getCreateTimeMs)
                    .orderByDesc(OperationLogPO::getId);
            return;
        }

        List<OrderBy> sortedOrders = orders.stream()
                .filter(order -> order.getField() != null)
                .sorted(Comparator.comparing(order -> order.getOrder() != null ? order.getOrder() : Integer.MAX_VALUE))
                .collect(Collectors.toList());

        for (OrderBy order : sortedOrders) {
            boolean isAsc = Boolean.TRUE.equals(order.getAscending());

            switch (order.getField()) {
                case BIZ_CODE:
                    wrapper.orderBy(true, isAsc, OperationLogPO::getBizCode);
                    break;
                case LOG_TYPE:
                    wrapper.orderBy(true, isAsc, OperationLogPO::getLogType);
                    break;
                case OPERATOR:
                    wrapper.orderBy(true, isAsc, OperationLogPO::getOperator);
                    break;
                case OPERATE_TIME_MS:
                    wrapper.orderBy(true, isAsc, OperationLogPO::getOperateTimeMs);
                    break;
                case CREATE_TIME_MS:
                    wrapper.orderBy(true, isAsc, OperationLogPO::getCreateTimeMs);
                    break;
                default:
                    wrapper.orderByDesc(OperationLogPO::getCreateTimeMs);
            }
        }
    }
}
