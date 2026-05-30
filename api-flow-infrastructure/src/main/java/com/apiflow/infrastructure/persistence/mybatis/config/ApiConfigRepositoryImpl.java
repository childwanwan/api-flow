package com.apiflow.infrastructure.persistence.mybatis.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.api.repository.config.idto.ApiConfigIDTO;
import com.apiflow.api.repository.config.param.*;
import com.apiflow.common.constant.SystemConstant;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.result.PageResult;
import com.apiflow.infrastructure.persistence.mybatis.config.converter.ApiConfigConverter;
import com.apiflow.infrastructure.persistence.mybatis.config.entity.ApiConfigPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createFieldResolver;

@Repository
@RequiredArgsConstructor
public class ApiConfigRepositoryImpl implements ApiConfigRepository {

    private final ApiConfigMapper apiConfigMapper;

    @Override
    public ApiConfigIDTO findByApiCode(String apiCode) {
        SelectOneApiConfigParam param = SelectOneApiConfigParam.builder()
                .condition(ConditionNode.eq(ApiConfigField.API_CODE.getFieldName(), apiCode))
                .selectFields(List.of(ApiConfigField.values()))
                .build();
        return selectOne(param);
    }

    @Override
    public void save(SaveApiConfigParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        ApiConfigPO configDO = ApiConfigConverter.INSTANCE.saveApiConfigParamToApiConfigEntityPO(param);
        apiConfigMapper.insert(configDO);
    }

    @Override
    public void update(UpdateApiConfigParam param) {
        if (ObjectUtil.isEmpty(param) || ObjectUtil.isEmpty(param.getId())) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        ApiConfigPO configDO = ApiConfigConverter.INSTANCE.updateApiConfigParamToApiConfigEntityPO(param);
        apiConfigMapper.updateById(configDO);
    }

    @Override
    public void deleteList(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            return;
        }
        if (idList.size() > SystemConstant.MAX_OPERATE_LIMIT) {
            throw new BusinessException(ErrorCode.DATA_TOO_LARGE);
        }
        apiConfigMapper.deleteByIds(idList);
    }

    @Override
    public ApiConfigIDTO selectOne(SelectOneApiConfigParam param) {
        if (ObjectUtil.isEmpty(param) || param.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<ApiConfigPO> wrapper = new QueryWrapper<>();
        if (CollectionUtil.isNotEmpty(param.getSelectFields())) {
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        }
        applyCondition(wrapper, param.getCondition());
        wrapper.orderByDesc("id").last("LIMIT 1");
        List<ApiConfigPO> list = apiConfigMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return ApiConfigConverter.INSTANCE.apiConfigEntityPOToApiConfigIDTO(list.get(0));
    }

    @Override
    public List<ApiConfigIDTO> selectList(SelectApiConfigParam param) {
        if (ObjectUtil.isEmpty(param) || CollectionUtil.isEmpty(param.getSelectFields())) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<ApiConfigPO> wrapper = new QueryWrapper<>();
        if (CollectionUtil.isNotEmpty(param.getSelectFields())) {
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        }
        applyCondition(wrapper, param.getCondition());
        applyListSorting(wrapper, param);
        wrapper.last("LIMIT " + param.getEffectiveLimit());
        List<ApiConfigPO> configDOList = apiConfigMapper.selectList(wrapper);
        return configDOList.stream()
                .map(ApiConfigConverter.INSTANCE::apiConfigEntityPOToApiConfigIDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<ApiConfigIDTO> selectPage(SelectPageApiConfigParam param) {
        if (ObjectUtil.isEmpty(param)
                || CollectionUtil.isEmpty(param.getSelectFields())) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<ApiConfigPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        applyCondition(wrapper, param.getCondition());
        applySorting(wrapper, param);

        Page<ApiConfigPO> page = new Page<>(param.getCurrent(), param.getSize());
        IPage<ApiConfigPO> result = apiConfigMapper.selectPage(page, wrapper);
        return ApiConfigConverter.INSTANCE.apiConfigPOIPage2PageResult(result);
    }

    private void applyCondition(QueryWrapper<ApiConfigPO> wrapper, ConditionNode condition) {
        QueryConditionHelper.applyConditionNode(wrapper, condition, createFieldResolver(ApiConfigField.values()));
    }

    private void applySorting(QueryWrapper<ApiConfigPO> wrapper, SelectPageApiConfigParam param) {
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

    private void applyListSorting(QueryWrapper<ApiConfigPO> wrapper, SelectApiConfigParam param) {
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

    @Override
    public Map<String, Integer> countByGroupNos(List<String> groupNos) {
        if (groupNos == null || groupNos.isEmpty()) {
            return new HashMap<>();
        }
        QueryWrapper<ApiConfigPO> wrapper = new QueryWrapper<>();
        wrapper.in(ApiConfigField.GROUP_NO.getColumnName(), groupNos);
        wrapper.select(ApiConfigField.GROUP_NO.getColumnName() + ", count(*) as cnt");
        wrapper.groupBy(ApiConfigField.GROUP_NO.getColumnName());
        List<Map<String, Object>> maps = apiConfigMapper.selectMaps(wrapper);
        Map<String, Integer> result = new HashMap<>();
        if (maps != null) {
            for (Map<String, Object> map : maps) {
                String groupNo = (String) map.get(ApiConfigField.GROUP_NO.getColumnName());
                Object cnt = map.get("cnt");
                if (groupNo != null && cnt != null) {
                    result.put(groupNo, ((Number) cnt).intValue());
                }
            }
        }
        return result;
    }
}
