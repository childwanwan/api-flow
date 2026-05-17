package com.apiflow.application.group.converter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.*;
import com.apiflow.application.group.dto.ApiGroupDTO;
import com.apiflow.application.group.param.ApiGroupListParam;
import com.apiflow.application.group.param.ApiGroupPageParam;
import com.apiflow.common.dto.SortOrder;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.domain.group.model.ApiGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ApiGroupConverter {
    ApiGroupConverter INSTANCE = Mappers.getMapper(ApiGroupConverter.class);

    SaveApiGroupParam toSaveParam(ApiGroup aggregate);

    UpdateApiGroupParam toUpdateParam(ApiGroup aggregate);

    default SelectPageApiGroupParam apiGroupPageParam2SelectPageApiGroupParam(ApiGroupPageParam param) {
        if (ObjectUtil.isEmpty(param)) {
            return null;
        }
        SelectPageApiGroupParam.SelectPageApiGroupParamBuilder paramBuilder = SelectPageApiGroupParam.builder();
        paramBuilder.selectFields(List.of(ApiGroupField.GROUP_NO,
                ApiGroupField.GROUP_CODE,
                ApiGroupField.GROUP_NAME,
                ApiGroupField.GROUP_DESCRIPTION,
                ApiGroupField.CREATE_TIME_MS));

        ConditionNode condition = buildConditionNode(param);
        if (condition != null) {
            paramBuilder.condition(condition);
        }

        paramBuilder.current(param.getEffectiveCurrent()).size(param.getEffectiveSize());

        List<OrderBy> orders = convertSortOrderList(param.getSortOrderList());
        if (!CollectionUtils.isEmpty(orders)) {
            paramBuilder.orders(orders);
        }
        return paramBuilder.build();
    }

    default SelectApiGroupParam apiGroupListParam2SelectApiGroupParam(ApiGroupListParam param) {
        SelectApiGroupParam.SelectApiGroupParamBuilder paramBuilder = SelectApiGroupParam.builder();
        paramBuilder.selectFields(List.of(ApiGroupField.GROUP_NO, ApiGroupField.GROUP_CODE, ApiGroupField.GROUP_NAME));
        paramBuilder.orders(List.of(OrderBy.desc(ApiGroupField.CREATE_TIME_MS), OrderBy.desc(ApiGroupField.ID)));
        return paramBuilder.build();
    }

    List<ApiGroupDTO> apiGroupIDTO2ApiGroupDTOList(List<ApiGroupIDTO> list);

    ApiGroupDTO apiGroupIDTO2ApiGroupDTO(ApiGroupIDTO apiGroupIDTO);

    private ConditionNode buildConditionNode(ApiGroupPageParam param) {
        List<ConditionNode> nodes = new ArrayList<>();
        if (StrUtil.isNotEmpty(param.getGroupNoLike())) {
            nodes.add(ConditionNode.like(ApiGroupField.GROUP_NO.getFieldName(), param.getGroupNoLike()));
        }
        if (StrUtil.isNotEmpty(param.getGroupCodeLike())) {
            nodes.add(ConditionNode.like(ApiGroupField.GROUP_CODE.getFieldName(), param.getGroupCodeLike()));
        }
        if (StrUtil.isNotEmpty(param.getGroupNameLike())) {
            nodes.add(ConditionNode.like(ApiGroupField.GROUP_NAME.getFieldName(), param.getGroupNameLike()));
        }
        if (nodes.isEmpty()) {
            return null;
        }
        return ConditionNode.and(nodes.toArray(new ConditionNode[0]));
    }

    private List<OrderBy> convertSortOrderList(List<SortOrder> sortOrderList) {
        if (CollectionUtils.isEmpty(sortOrderList)) {
            return List.of(OrderBy.desc(ApiGroupField.CREATE_TIME_MS), OrderBy.desc(ApiGroupField.ID));
        }

        return sortOrderList.stream()
                .filter(order -> StrUtil.isNotEmpty(order.getField()))
                .map(order -> {
                    try {
                        ApiGroupField field = ApiGroupField.valueOf(order.getField().toUpperCase());
                        return OrderBy.builder()
                                .field(field)
                                .ascending(ObjectUtil.isNotEmpty(order.getAscending()) ? order.getAscending() : true)
                                .order(order.getOrder())
                                .build();
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(ObjectUtil::isNotEmpty)
                .sorted(Comparator.comparing(order -> ObjectUtil.isNotEmpty(order.getOrder()) ? order.getOrder() : Integer.MAX_VALUE))
                .collect(Collectors.toList());
    }
}
