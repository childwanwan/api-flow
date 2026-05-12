package com.apiflow.infrastructure.persistence.mybatis.group;

import com.apiflow.api.repository.group.ApiGroupRepository;
import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.*;
import com.apiflow.common.result.PageResult;
import com.apiflow.infrastructure.persistence.mybatis.group.converter.ApiGroupConverter;
import com.apiflow.infrastructure.persistence.mybatis.group.entity.ApiGroupPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        ApiGroupPO po = ApiGroupConverter.INSTANCE.saveParamToPO(param);
        apiGroupMapper.insert(po);
        return ApiGroupConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public ApiGroupIDTO update(UpdateApiGroupParam param) {
        ApiGroupPO po = ApiGroupConverter.INSTANCE.updateParamToPO(param);
        apiGroupMapper.updateById(po);
        return ApiGroupConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public ApiGroupIDTO selectOne(SelectOneApiGroupParam param) {
        LambdaQueryWrapper<ApiGroupPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupNo, param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupCode, param.getGroupCode());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupName, param.getGroupName());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(ApiGroupField.values()));
        ApiGroupPO po = apiGroupMapper.selectOne(wrapper);
        return po == null ? null : ApiGroupConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public List<ApiGroupIDTO> selectList(SelectApiGroupParam param) {
        LambdaQueryWrapper<ApiGroupPO> wrapper = new LambdaQueryWrapper<>();
        applyPageConditions(wrapper, param);
        wrapper.orderByDesc(ApiGroupPO::getCreateTimeMs);

        if (param.getOffset() != null && param.getLimit() != null) {
            wrapper.last("LIMIT " + param.getLimit() + " OFFSET " + param.getOffset());
        } else if (param.getLimit() != null) {
            wrapper.last("LIMIT " + param.getLimit());
        }

        List<ApiGroupPO> list = apiGroupMapper.selectList(wrapper);
        return list.stream()
                .map(ApiGroupConverter.INSTANCE::poToIDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<ApiGroupIDTO> selectPage(SelectPageApiGroupParam param) {
        LambdaQueryWrapper<ApiGroupPO> wrapper = new LambdaQueryWrapper<>();
        applyPageConditions(wrapper, param);
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

    private void applyPageConditions(LambdaQueryWrapper<ApiGroupPO> wrapper, SelectApiGroupParam param) {
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupNo, param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupCode, param.getGroupCode());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupName, param.getGroupName());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(ApiGroupField.values()));
    }

    private void applyPageConditions(LambdaQueryWrapper<ApiGroupPO> wrapper, SelectPageApiGroupParam param) {
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(ApiGroupField.values()));
    }

    private void applySorting(LambdaQueryWrapper<ApiGroupPO> wrapper, SelectPageApiGroupParam param) {
        List<OrderBy> orders = param.getOrders();
        if (orders == null || orders.isEmpty()) {
            wrapper.orderByDesc(ApiGroupPO::getUpdateTimeMs)
                    .orderByDesc(ApiGroupPO::getId);
            return;
        }

        List<OrderBy> sortedOrders = orders.stream()
                .filter(order -> order.getField() != null)
                .sorted(Comparator.comparing(order -> order.getOrder() != null ? order.getOrder() : Integer.MAX_VALUE))
                .collect(Collectors.toList());

        for (OrderBy order : sortedOrders) {
            boolean isAsc = Boolean.TRUE.equals(order.getAscending());

            switch (order.getField()) {
                case GROUP_NO:
                    wrapper.orderBy(true, isAsc, ApiGroupPO::getGroupNo);
                    break;
                case GROUP_CODE:
                    wrapper.orderBy(true, isAsc, ApiGroupPO::getGroupCode);
                    break;
                case GROUP_NAME:
                    wrapper.orderBy(true, isAsc, ApiGroupPO::getGroupName);
                    break;
                case CREATE_TIME_MS:
                    wrapper.orderBy(true, isAsc, ApiGroupPO::getCreateTimeMs);
                    break;
                case UPDATE_TIME_MS:
                    wrapper.orderBy(true, isAsc, ApiGroupPO::getUpdateTimeMs);
                    break;
                default:
                    wrapper.orderByDesc(ApiGroupPO::getCreateTimeMs);
            }
        }
    }
}
