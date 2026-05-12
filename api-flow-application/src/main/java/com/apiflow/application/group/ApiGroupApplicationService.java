package com.apiflow.application.group;

import com.apiflow.api.repository.group.ApiGroupRepository;
import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.SelectPageApiGroupParam;
import com.apiflow.application.group.converter.ApiGroupConverter;
import com.apiflow.application.group.dto.ApiGroupDTO;
import com.apiflow.application.group.param.ApiGroupCreateParam;
import com.apiflow.application.group.param.ApiGroupDeleteParam;
import com.apiflow.application.group.param.ApiGroupGetParam;
import com.apiflow.application.group.param.ApiGroupPageParam;
import com.apiflow.application.group.param.ApiGroupUpdateParam;
import com.apiflow.common.result.PageResult;
import com.apiflow.domain.group.command.CreateApiGroupCommand;
import com.apiflow.domain.group.command.DeleteApiGroupCommand;
import com.apiflow.domain.group.command.UpdateApiGroupCommand;
import com.apiflow.domain.group.model.ApiGroup;
import com.apiflow.domain.group.service.ApiGroupDomainService;
import com.apiflow.api.mq.MessageProducer;
import com.apiflow.domain.shared.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiGroupApplicationService {

    private final ApiGroupDomainService apiGroupDomainService;
    private final ApiGroupRepository apiGroupRepository;
    private final MessageProducer messageProducer;

    private static final String TOPIC = "api-group-event";

    public ApiGroupDTO createGroup(ApiGroupCreateParam param) {
        CreateApiGroupCommand command = CreateApiGroupCommand.builder()
                .groupCode(param.getGroupCode())
                .groupName(param.getGroupName())
                .groupDescription(param.getGroupDescription())
                .operator(param.getOperator())
                .build();
        ApiGroup group = apiGroupDomainService.create(command);
        publishDomainEvents(group);
        return toDTO(group);
    }

    public ApiGroupDTO updateGroup(ApiGroupUpdateParam param) {
        UpdateApiGroupCommand command = UpdateApiGroupCommand.builder()
                .groupNo(param.getGroupNo())
                .groupCode(param.getGroupCode())
                .groupName(param.getGroupName())
                .groupDescription(param.getGroupDescription())
                .operator(param.getOperator())
                .build();
        ApiGroup group = apiGroupDomainService.update(command);
        publishDomainEvents(group);
        return toDTO(group);
    }

    public ApiGroupDTO getGroup(ApiGroupGetParam param) {
        ApiGroup group = apiGroupDomainService.getByGroupNo(param.getGroupNo());
        return toDTO(group);
    }

    public void deleteGroup(ApiGroupDeleteParam param) {
        DeleteApiGroupCommand command = DeleteApiGroupCommand.builder()
                .groupNo(param.getGroupNo())
                .operator(param.getOperator())
                .build();
        ApiGroup group = apiGroupDomainService.delete(command);
        publishDomainEvents(group);
    }

    public PageResult<ApiGroupDTO> pageGroups(ApiGroupPageParam param) {
        SelectPageApiGroupParam queryParam = ApiGroupConverter.INSTANCE.apiGroupPageParam2SelectPageApiGroupParam(param);
        PageResult<ApiGroupIDTO> page = apiGroupRepository.selectPage(queryParam);
        return PageResult.of(ApiGroupConverter.INSTANCE.apiGroupIDTO2ApiGroupDTOList(page.getRecords()),
                page.getTotal(), param.getEffectiveCurrent(), param.getEffectiveSize());
    }

    private void publishDomainEvents(ApiGroup group) {
        for (DomainEvent event : group.getDomainEvents()) {
            try {
                Map<String, String> headers = Map.of(
                        "eventId", event.getEventId(),
                        "eventType", event.getEventType(),
                        "aggregateType", event.getAggregateType(),
                        "aggregateId", event.getAggregateId()
                );
                messageProducer.send(TOPIC, event, headers);
                log.info("Published domain event to Kafka: type={}, aggregateId={}", event.getEventType(), event.getAggregateId());
            } catch (Exception e) {
                log.error("Failed to publish domain event to Kafka: type={}, aggregateId={}, eventId={}",
                        event.getEventType(), event.getAggregateId(), event.getEventId(), e);
            }
        }
        group.clearDomainEvents();
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
