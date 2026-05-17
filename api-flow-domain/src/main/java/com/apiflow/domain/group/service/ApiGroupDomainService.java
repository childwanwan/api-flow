package com.apiflow.domain.group.service;

import com.apiflow.api.repository.group.ApiGroupRepository;
import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.ApiGroupField;
import com.apiflow.api.repository.group.param.SelectOneApiGroupParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.domain.group.command.CreateApiGroupCommand;
import com.apiflow.domain.group.command.DeleteApiGroupCommand;
import com.apiflow.domain.group.command.UpdateApiGroupCommand;
import com.apiflow.domain.group.converter.ApiGroupConverter;
import com.apiflow.domain.group.model.ApiGroup;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ApiGroupDomainService {

    private final ApiGroupRepository apiGroupRepository;
    private static final ApiGroupConverter CONVERTER = ApiGroupConverter.INSTANCE;

    public ApiGroup create(CreateApiGroupCommand command) {
        SelectOneApiGroupParam param = SelectOneApiGroupParam.builder()
                .condition(ConditionNode.eq(ApiGroupField.GROUP_CODE.getFieldName(), command.getGroupCode()))
                .selectFields(List.of(ApiGroupField.GROUP_NO))
                .build();
        if (apiGroupRepository.selectOne(param) != null) {
            throw new BusinessException(ErrorCode.GROUP_CODE_EXIST);
        }

        return ApiGroup.create(
                command.getGroupCode(),
                command.getGroupName(),
                command.getGroupDescription(),
                command.getOperator()
        );
    }

    public ApiGroup update(UpdateApiGroupCommand command) {
        ApiGroup group = getByGroupNo(command.getGroupNo(),
                ApiGroupField.ID,
                ApiGroupField.GROUP_NO,
                ApiGroupField.GROUP_CODE,
                ApiGroupField.GROUP_NAME,
                ApiGroupField.GROUP_DESCRIPTION
        );

        if (!group.isSameGroupCode(command.getGroupCode())) {
            checkGroupCodeNotOccupied(command.getGroupCode(), command.getGroupNo());
        }

        group.update(
                command.getGroupCode(),
                command.getGroupName(),
                command.getGroupDescription(),
                command.getOperator()
        );

        return group;
    }

    public ApiGroup delete(DeleteApiGroupCommand command) {
        ApiGroup group = getByGroupNo(command.getGroupNo(),
                ApiGroupField.ID,
                ApiGroupField.GROUP_NO
        );
        group.delete(command.getOperator());
        return group;
    }

    private ApiGroup getByGroupNo(String groupNo, ApiGroupField... selectFields) {
        SelectOneApiGroupParam param = SelectOneApiGroupParam.builder()
                .condition(ConditionNode.eq(ApiGroupField.GROUP_NO.getFieldName(), groupNo))
                .selectFields(List.of(selectFields))
                .build();
        ApiGroupIDTO dto = apiGroupRepository.selectOne(param);
        if (dto == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST);
        }
        return CONVERTER.toAggregate(dto);
    }

    private void checkGroupCodeNotOccupied(String groupCode, String excludeGroupNo) {
        SelectOneApiGroupParam param = SelectOneApiGroupParam.builder()
                .condition(ConditionNode.eq(ApiGroupField.GROUP_CODE.getFieldName(), groupCode))
                .selectFields(List.of(ApiGroupField.GROUP_NO))
                .build();
        ApiGroupIDTO existing = apiGroupRepository.selectOne(param);
        if (existing != null && !existing.getGroupNo().equals(excludeGroupNo)) {
            throw new BusinessException(ErrorCode.GROUP_CODE_EXIST);
        }
    }
}
