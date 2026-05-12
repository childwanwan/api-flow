package com.apiflow.application.group.converter;

import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.*;
import com.apiflow.application.group.dto.ApiGroupDTO;
import com.apiflow.application.group.param.ApiGroupPageParam;
import com.apiflow.common.dto.SortOrder;
import com.apiflow.common.repository.ConditionNode;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

    default SelectPageApiGroupParam apiGroupPageParam2SelectPageApiGroupParam(ApiGroupPageParam param) {
        if (ObjectUtils.isEmpty(param)) {
            return null;
        }
        SelectPageApiGroupParam.SelectPageApiGroupParamBuilder paramBuilder = SelectPageApiGroupParam.builder();

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

    List<ApiGroupDTO> apiGroupIDTO2ApiGroupDTOList(List<ApiGroupIDTO> list);

    ApiGroupDTO apiGroupIDTO2ApiGroupDTO(ApiGroupIDTO apiGroupIDTO);

    private ConditionNode buildConditionNode(ApiGroupPageParam param) {
        List<ConditionNode> nodes = new ArrayList<>();
        if (StringUtils.isNotEmpty(param.getGroupNoLike())) {
            nodes.add(ConditionNode.like(ApiGroupField.GROUP_NO.getFieldName(), param.getGroupNoLike()));
        }
        if (StringUtils.isNotEmpty(param.getGroupCodeLike())) {
            nodes.add(ConditionNode.like(ApiGroupField.GROUP_CODE.getFieldName(), param.getGroupCodeLike()));
        }
        if (StringUtils.isNotEmpty(param.getGroupNameLike())) {
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
                .filter(order -> StringUtils.isNotEmpty(order.getField()))
                .map(order -> {
                    try {
                        ApiGroupField field = ApiGroupField.valueOf(order.getField().toUpperCase());
                        return OrderBy.builder()
                                .field(field)
                                .ascending(ObjectUtils.isNotEmpty(order.getAscending()) ? order.getAscending() : true)
                                .order(order.getOrder())
                                .build();
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(ObjectUtils::isNotEmpty)
                .sorted(Comparator.comparing(order -> ObjectUtils.isNotEmpty(order.getOrder()) ? order.getOrder() : Integer.MAX_VALUE))
                .collect(Collectors.toList());
    }
}
