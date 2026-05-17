package com.apiflow.application.group;

import cn.hutool.core.util.ObjectUtil;
import com.apiflow.api.repository.group.ApiGroupRepository;
import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.ApiGroupField;
import com.apiflow.api.repository.group.param.SelectApiGroupParam;
import com.apiflow.api.repository.group.param.SelectOneApiGroupParam;
import com.apiflow.api.repository.group.param.SelectPageApiGroupParam;
import com.apiflow.application.LockHelper;
import com.apiflow.application.group.converter.ApiGroupConverter;
import com.apiflow.application.group.dto.ApiGroupDTO;
import com.apiflow.application.group.param.*;
import com.apiflow.common.constant.LockPrefix;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.ConditionNode;
import com.apiflow.common.result.PageResult;
import com.apiflow.domain.group.command.CreateApiGroupCommand;
import com.apiflow.domain.group.command.DeleteApiGroupCommand;
import com.apiflow.domain.group.command.UpdateApiGroupCommand;
import com.apiflow.domain.group.model.ApiGroup;
import com.apiflow.domain.group.service.ApiGroupDomainService;
import com.apiflow.domain.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiGroupApplicationService {

    private static final String LOCK_PREFIX = LockPrefix.API_GROUP;

    private static final ApiGroupConverter CONVERTER = ApiGroupConverter.INSTANCE;

    private final ApiGroupDomainService apiGroupDomainService;
    private final ApiGroupRepository apiGroupRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final LockHelper lockHelper;

    public ApiGroupDTO createGroup(ApiGroupCreateParam param) {
        return lockHelper.execute(LOCK_PREFIX, param.getGroupCode(), () -> {
            CreateApiGroupCommand command = CreateApiGroupCommand.builder()
                    .groupCode(param.getGroupCode())
                    .groupName(param.getGroupName())
                    .groupDescription(param.getGroupDescription())
                    .operator(param.getOperator())
                    .build();
            ApiGroup group = apiGroupDomainService.create(command);
            apiGroupRepository.save(CONVERTER.toSaveParam(group));
            domainEventPublisher.publishAll(group);
            return toDTO(group);
        });
    }

    public ApiGroupDTO updateGroup(ApiGroupUpdateParam param) {
        return lockHelper.execute(LOCK_PREFIX, param.getGroupNo(), () -> {
            UpdateApiGroupCommand command = UpdateApiGroupCommand.builder()
                    .groupNo(param.getGroupNo())
                    .groupCode(param.getGroupCode())
                    .groupName(param.getGroupName())
                    .groupDescription(param.getGroupDescription())
                    .operator(param.getOperator())
                    .build();
            ApiGroup group = apiGroupDomainService.update(command);
            apiGroupRepository.update(CONVERTER.toUpdateParam(group));
            domainEventPublisher.publishAll(group);
            return toDTO(group);
        });
    }

    public ApiGroupDTO getGroup(ApiGroupGetParam param) {
        SelectOneApiGroupParam queryParam = SelectOneApiGroupParam.builder()
                .condition(ConditionNode.eq(ApiGroupField.GROUP_NO.getFieldName(), param.getGroupNo()))
                .selectFields(List.of(ApiGroupField.GROUP_NO,
                        ApiGroupField.GROUP_CODE,
                        ApiGroupField.GROUP_NAME,
                        ApiGroupField.GROUP_DESCRIPTION,
                        ApiGroupField.CREATE_TIME_MS))
                .build();
        ApiGroupIDTO idto = apiGroupRepository.selectOne(queryParam);
        if (ObjectUtil.isEmpty(idto)) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST);
        }
        return CONVERTER.apiGroupIDTO2ApiGroupDTO(idto);
    }

    public void deleteGroup(ApiGroupDeleteParam param) {
        lockHelper.executeVoid(LOCK_PREFIX, param.getGroupNo(), () -> {
            DeleteApiGroupCommand command = DeleteApiGroupCommand.builder()
                    .groupNo(param.getGroupNo())
                    .operator(param.getOperator())
                    .build();
            ApiGroup group = apiGroupDomainService.delete(command);
            apiGroupRepository.deleteList(List.of(group.getId()));
            domainEventPublisher.publishAll(group);
        });
    }

    public PageResult<ApiGroupDTO> pageGroups(ApiGroupPageParam param) {
        SelectPageApiGroupParam queryParam = CONVERTER.apiGroupPageParam2SelectPageApiGroupParam(param);
        PageResult<ApiGroupIDTO> page = apiGroupRepository.selectPage(queryParam);
        return PageResult.of(CONVERTER.apiGroupIDTO2ApiGroupDTOList(page.getRecords()),
                page.getTotal(), param.getEffectiveCurrent(), param.getEffectiveSize());
    }

    public List<ApiGroupDTO> listGroups(ApiGroupListParam param) {
        SelectApiGroupParam queryParam = CONVERTER.apiGroupListParam2SelectApiGroupParam(param);
        List<ApiGroupIDTO> list = apiGroupRepository.selectList(queryParam);
        return CONVERTER.apiGroupIDTO2ApiGroupDTOList(list);
    }

    private ApiGroupDTO toDTO(ApiGroup group) {
        return ApiGroupDTO.builder()
                .id(group.getId())
                .groupNo(group.getGroupNo())
                .groupCode(group.getGroupCode())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .createTimeMs(group.getCreateTimeMs())
                .updateTimeMs(group.getUpdateTimeMs())
                .createOperator(group.getCreateOperator())
                .updateOperator(group.getUpdateOperator())
                .build();
    }
}
