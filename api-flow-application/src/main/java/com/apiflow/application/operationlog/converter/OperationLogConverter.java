package com.apiflow.application.operationlog.converter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.OperationLogField;
import com.apiflow.api.repository.operationlog.param.OrderBy;
import com.apiflow.api.repository.operationlog.param.SaveOperationLogParam;
import com.apiflow.api.repository.operationlog.param.SelectPageOperationLogParam;
import com.apiflow.application.operationlog.dto.OperationLogDTO;
import com.apiflow.application.operationlog.param.OperationLogCreateParam;
import com.apiflow.application.operationlog.param.OperationLogPageParam;
import com.apiflow.common.dto.SortOrder;
import com.apiflow.common.repository.ConditionNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface OperationLogConverter {
    OperationLogConverter INSTANCE = Mappers.getMapper(OperationLogConverter.class);

    SaveOperationLogParam operationLogCreateParam2SaveOperationLogParam(OperationLogCreateParam param);

    @Mapping(target = "showDetail", source = "showDetail")
    OperationLogDTO operationLogIDTO2OperationLogDTO(OperationLogIDTO idto);

    List<OperationLogDTO> operationLogIDTO2OperationLogDTOList(List<OperationLogIDTO> list);

    default SelectPageOperationLogParam operationLogPageParam2SelectPageOperationLogParam(OperationLogPageParam param) {
        if (ObjectUtil.isEmpty(param)) {
            return null;
        }
        SelectPageOperationLogParam.SelectPageOperationLogParamBuilder paramBuilder = SelectPageOperationLogParam.builder();

        paramBuilder.selectFields(List.of(
                OperationLogField.BIZ_CODE,
                OperationLogField.LOG_TYPE,
                OperationLogField.OPERATOR,
                OperationLogField.OPERATE_TIME_MS,
                OperationLogField.LOG_DATA));

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

    private ConditionNode buildConditionNode(OperationLogPageParam param) {
        List<ConditionNode> nodes = new ArrayList<>();
        if (StrUtil.isNotEmpty(param.getBizCode())) {
            nodes.add(ConditionNode.eq(OperationLogField.BIZ_CODE.getFieldName(), param.getBizCode()));
        }
        if (StrUtil.isNotEmpty(param.getLogType())) {
            nodes.add(ConditionNode.eq(OperationLogField.LOG_TYPE.getFieldName(), param.getLogType()));
        }
        if (StrUtil.isNotEmpty(param.getOperator())) {
            nodes.add(ConditionNode.like(OperationLogField.OPERATOR.getFieldName(), param.getOperator()));
        }
        if (param.getOperateTimeMsStart() != null && param.getOperateTimeMsEnd() != null) {
            nodes.add(ConditionNode.between(OperationLogField.OPERATE_TIME_MS.getFieldName(),
                    param.getOperateTimeMsStart(), param.getOperateTimeMsEnd()));
        } else if (param.getOperateTimeMsStart() != null) {
            nodes.add(ConditionNode.ge(OperationLogField.OPERATE_TIME_MS.getFieldName(), param.getOperateTimeMsStart()));
        } else if (param.getOperateTimeMsEnd() != null) {
            nodes.add(ConditionNode.le(OperationLogField.OPERATE_TIME_MS.getFieldName(), param.getOperateTimeMsEnd()));
        }
        if (nodes.isEmpty()) {
            return null;
        }
        return ConditionNode.and(nodes.toArray(new ConditionNode[0]));
    }

    private List<OrderBy> convertSortOrderList(List<SortOrder> sortOrderList) {
        if (CollectionUtils.isEmpty(sortOrderList)) {
            return List.of(OrderBy.desc(OperationLogField.CREATE_TIME_MS), OrderBy.desc(OperationLogField.ID));
        }

        return sortOrderList.stream()
                .filter(order -> StrUtil.isNotEmpty(order.getField()))
                .map(order -> {
                    try {
                        OperationLogField field = OperationLogField.valueOf(order.getField().toUpperCase());
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
