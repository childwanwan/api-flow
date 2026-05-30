package com.apiflow.application.group;

import cn.hutool.core.util.ObjectUtil;
import com.apiflow.api.repository.config.ApiConfigRepository;
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
import com.apiflow.application.shared.event.DomainEventConverterFacade;
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
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiGroupApplicationService {

    private static final String LOCK_PREFIX = LockPrefix.API_GROUP;

    private static final ApiGroupConverter CONVERTER = ApiGroupConverter.INSTANCE;

    private final ApiGroupDomainService apiGroupDomainService;
    private final ApiGroupRepository apiGroupRepository;
    private final ApiConfigRepository apiConfigRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final DomainEventConverterFacade domainEventConverterFacade;
    private final LockHelper lockHelper;
    private final TransactionTemplate transactionTemplate;

    public void createGroup(CreateApiGroupParam param) {
        lockHelper.executeVoid(LOCK_PREFIX, param.getGroupCode(), () -> {
            CreateApiGroupCommand command = CreateApiGroupCommand.builder()
                    .groupCode(param.getGroupCode())
                    .groupName(param.getGroupName())
                    .groupDescription(param.getGroupDescription())
                    .operator(param.getOperator())
                    .build();
            ApiGroup group = apiGroupDomainService.create(command);
            transactionTemplate.executeWithoutResult(status -> {
                apiGroupRepository.save(CONVERTER.toSaveParam(group));
            });
            domainEventPublisher.publishAll(domainEventConverterFacade.convert(group));
        });
    }

    public void updateGroup(UpdateApiGroupParam param) {
        lockHelper.executeVoid(LOCK_PREFIX, param.getGroupNo(), () -> {
            UpdateApiGroupCommand command = UpdateApiGroupCommand.builder()
                    .groupNo(param.getGroupNo())
                    .groupCode(param.getGroupCode())
                    .groupName(param.getGroupName())
                    .groupDescription(param.getGroupDescription())
                    .operator(param.getOperator())
                    .build();
            ApiGroup group = apiGroupDomainService.update(command);
            transactionTemplate.executeWithoutResult(status -> {
                apiGroupRepository.update(CONVERTER.toUpdateParam(group));
            });
            domainEventPublisher.publishAll(domainEventConverterFacade.convert(group));
        });
    }

    public ApiGroupDTO getGroup(GetApiGroupParam param) {
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

    public void deleteGroup(DeleteApiGroupParam param) {
        lockHelper.executeVoid(LOCK_PREFIX, param.getGroupNo(), () -> {
            DeleteApiGroupCommand command = DeleteApiGroupCommand.builder()
                    .groupNo(param.getGroupNo())
                    .operator(param.getOperator())
                    .build();
            ApiGroup group = apiGroupDomainService.delete(command);
            transactionTemplate.executeWithoutResult(status -> {
                apiGroupRepository.deleteList(List.of(group.getId()));
            });
            domainEventPublisher.publishAll(domainEventConverterFacade.convert(group));
        });
    }

    public PageResult<ApiGroupDTO> pageGroups(PageApiGroupParam param) {
        SelectPageApiGroupParam queryParam = CONVERTER.apiGroupPageParam2SelectPageApiGroupParam(param);
        PageResult<ApiGroupIDTO> page = apiGroupRepository.selectPage(queryParam);
        List<ApiGroupDTO> dtoList = CONVERTER.apiGroupIDTO2ApiGroupDTOList(page.getRecords());

        List<String> groupNos = dtoList.stream()
                .map(ApiGroupDTO::getGroupNo)
                .filter(ObjectUtil::isNotEmpty)
                .distinct()
                .toList();
        List<ApiGroupDTO> dtoListWithCount = dtoList;
        if (!groupNos.isEmpty()) {
            Map<String, Integer> countMap = apiConfigRepository.countByGroupNos(groupNos);
            dtoListWithCount = dtoList.stream()
                    .map(dto -> ApiGroupDTO.builder()
                            .id(dto.getId())
                            .groupNo(dto.getGroupNo())
                            .groupCode(dto.getGroupCode())
                            .groupName(dto.getGroupName())
                            .groupDescription(dto.getGroupDescription())
                            .createTimeMs(dto.getCreateTimeMs())
                            .updateTimeMs(dto.getUpdateTimeMs())
                            .createOperator(dto.getCreateOperator())
                            .updateOperator(dto.getUpdateOperator())
                            .apiCount(countMap.getOrDefault(dto.getGroupNo(), 0))
                            .build())
                    .toList();
        }

        return PageResult.of(dtoListWithCount,
                page.getTotal(), param.getEffectiveCurrent(), param.getEffectiveSize());
    }

    public List<ApiGroupDTO> listGroups(ListApiGroupParam param) {
        SelectApiGroupParam queryParam = CONVERTER.apiGroupListParam2SelectApiGroupParam(param);
        List<ApiGroupIDTO> list = apiGroupRepository.selectList(queryParam);
        return CONVERTER.apiGroupIDTO2ApiGroupDTOList(list);
    }

}
