package com.apiflow.infrastructure.persistence.mybatis.group;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.apiflow.api.repository.group.ApiGroupRepository;
import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.*;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.result.PageResult;
import com.apiflow.infrastructure.persistence.mybatis.group.converter.ApiGroupConverter;
import com.apiflow.infrastructure.persistence.mybatis.group.entity.ApiGroupPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createFieldResolver;

@Repository
@RequiredArgsConstructor
public class ApiGroupRepositoryImpl implements ApiGroupRepository {

    private final ApiGroupMapper apiGroupMapper;

    @Override
    public ApiGroupIDTO save(SaveApiGroupParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        ApiGroupPO po = ApiGroupConverter.INSTANCE.saveParamToPO(param);
        apiGroupMapper.insert(po);
        return ApiGroupConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public ApiGroupIDTO update(UpdateApiGroupParam param) {
        if (ObjectUtil.isEmpty(param) || ObjectUtil.isEmpty(param.getId())) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        ApiGroupPO po = ApiGroupConverter.INSTANCE.updateParamToPO(param);
        apiGroupMapper.updateById(po);
        return ApiGroupConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public ApiGroupIDTO selectOne(SelectOneApiGroupParam param) {
        if (ObjectUtil.isEmpty(param) || param.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<ApiGroupPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        applyCondition(wrapper, param.getCondition());
        ApiGroupPO po = apiGroupMapper.selectOne(wrapper);
        return po == null ? null : ApiGroupConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public List<ApiGroupIDTO> selectList(SelectApiGroupParam param) {
        if (ObjectUtil.isEmpty(param)
                || CollectionUtil.isEmpty(param.getSelectFields())) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<ApiGroupPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        applyCondition(wrapper, param.getCondition());
        applyListSorting(wrapper, param);
        wrapper.last("LIMIT " + param.getEffectiveLimit());

        List<ApiGroupPO> list = apiGroupMapper.selectList(wrapper);
        return list.stream()
                .map(ApiGroupConverter.INSTANCE::poToIDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<ApiGroupIDTO> selectPage(SelectPageApiGroupParam param) {
        if (ObjectUtil.isEmpty(param)
                || CollectionUtil.isEmpty(param.getSelectFields())) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<ApiGroupPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        applyCondition(wrapper, param.getCondition());
        applySorting(wrapper, param);

        Page<ApiGroupPO> page = new Page<>(param.getCurrent(), param.getSize());
        IPage<ApiGroupPO> result = apiGroupMapper.selectPage(page, wrapper);
        return ApiGroupConverter.INSTANCE.apiGroupPOIPage2PageResult(result);
    }

    @Override
    public void deleteList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        apiGroupMapper.deleteByIds(idList);
    }

    private void applyCondition(QueryWrapper<ApiGroupPO> wrapper, ConditionNode condition) {
        QueryConditionHelper.applyConditionNode(wrapper, condition, createFieldResolver(ApiGroupField.values()));
    }

    private void applySorting(QueryWrapper<ApiGroupPO> wrapper, SelectPageApiGroupParam param) {
        List<OrderBy> orders = param.getOrders();
        if (orders == null || orders.isEmpty()) {
            wrapper.orderByDesc("update_time_ms").orderByDesc("id");
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

    private void applyListSorting(QueryWrapper<ApiGroupPO> wrapper, SelectApiGroupParam param) {
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
