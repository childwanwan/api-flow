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
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

@RequiredArgsConstructor
public class ApiGroupDomainService {

    private final ApiGroupRepository apiGroupRepository;
    private static final ApiGroupConverter CONVERTER = ApiGroupConverter.INSTANCE;

    public ApiGroup create(CreateApiGroupCommand command) {
        checkGroupCodeUnique(command.getGroupCode(), null);

        ApiGroup group = ApiGroup.create(
                command.getGroupCode(),
                command.getGroupName(),
                command.getGroupDescription(),
                command.getOperator()
        );
        apiGroupRepository.save(CONVERTER.toSaveParam(group));
        return group;
    }

    public ApiGroup update(UpdateApiGroupCommand command) {
        ApiGroup group = getByGroupNo(command.getGroupNo());

        if (!group.isSameGroupCode(command.getGroupCode())) {
            checkGroupCodeUnique(command.getGroupCode(), command.getGroupNo());
        }

        group.update(
                command.getGroupCode(),
                command.getGroupName(),
                command.getGroupDescription(),
                command.getOperator()
        );

        apiGroupRepository.update(CONVERTER.toUpdateParam(group));
        return group;
    }

    public ApiGroup getByGroupNo(String groupNo) {
        SelectOneApiGroupParam param = SelectOneApiGroupParam.builder()
                .condition(ConditionNode.eq(ApiGroupField.GROUP_NO.getFieldName(), groupNo))
                .build();
        ApiGroupIDTO dto = apiGroupRepository.selectOne(param);
        if (ObjectUtils.isEmpty(dto)) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST);
        }
        return CONVERTER.toAggregate(dto);
    }

    public ApiGroup delete(DeleteApiGroupCommand command) {
        ApiGroup group = getByGroupNo(command.getGroupNo());
        group.delete(command.getOperator());

        apiGroupRepository.deleteList(List.of(group.getId()));
        return group;
    }

    private void checkGroupCodeUnique(String groupCode, String excludeGroupNo) {
        SelectOneApiGroupParam param = SelectOneApiGroupParam.builder()
                .condition(ConditionNode.eq(ApiGroupField.GROUP_CODE.getFieldName(), groupCode))
                .build();
        ApiGroupIDTO existing = apiGroupRepository.selectOne(param);
        if (ObjectUtils.isNotEmpty(existing)) {
            if (excludeGroupNo == null || !excludeGroupNo.equals(existing.getGroupNo())) {
                throw new BusinessException(ErrorCode.GROUP_CODE_EXIST);
            }
        }
    }
}
