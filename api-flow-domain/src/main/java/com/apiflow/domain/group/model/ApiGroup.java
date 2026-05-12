package com.apiflow.domain.group.model;

import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.util.CodeUtil;
import com.apiflow.domain.group.event.ApiGroupDomainEvent;
import com.apiflow.domain.shared.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiGroup {

    private Long id;
    private String groupNo;
    private String groupCode;
    private String groupName;
    private String groupDescription;
    private Long createTimeMs;
    private Long updateTimeMs;
    private String createOperator;
    private String updateOperator;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public static ApiGroup create(String groupCode, String groupName, String groupDescription, String operator) {
        validateGroupCode(groupCode);
        validateGroupName(groupName);

        long now = System.currentTimeMillis();
        ApiGroup group = new ApiGroup();
        group.groupNo = CodeUtil.generateCode(8);
        group.groupCode = groupCode;
        group.groupName = groupName;
        group.groupDescription = groupDescription;
        group.createTimeMs = now;
        group.updateTimeMs = now;
        group.createOperator = operator;
        group.updateOperator = operator;

        group.registerEvent(new ApiGroupDomainEvent.Created(
                group.groupNo, groupCode, groupName, groupDescription, operator
        ));

        return group;
    }

    public void update(String groupCode, String groupName, String groupDescription, String operator) {
        String oldGroupCode = this.groupCode;
        String oldGroupName = this.groupName;
        String oldGroupDescription = this.groupDescription;


        if (StringUtils.isNotBlank(groupCode)) {
            this.groupCode = groupCode;
        }
        if (StringUtils.isNotBlank(groupName)) {
            this.groupName = groupName;
        }
        if (groupDescription != null) {
            this.groupDescription = groupDescription;
        }
        this.updateOperator = operator;
        this.updateTimeMs = System.currentTimeMillis();

        registerEvent(new ApiGroupDomainEvent.Updated(
                this.groupNo, this.groupCode, this.groupName,
                this.groupDescription, operator,
                oldGroupCode, oldGroupName, oldGroupDescription
        ));
    }

    public void delete(String operator) {
        this.updateOperator = operator;
        this.updateTimeMs = System.currentTimeMillis();

        registerEvent(new ApiGroupDomainEvent.Deleted(
                this.groupNo, this.groupCode, this.groupName, operator
        ));
    }

    public boolean isSameGroupCode(String otherGroupCode) {
        return this.groupCode != null && this.groupCode.equals(otherGroupCode);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    private void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    private static void validateGroupCode(String groupCode) {
        if (StringUtils.isBlank(groupCode)) {
            throw new BusinessException(ErrorCode.GROUP_CODE_EMPTY);
        }
    }

    private static void validateGroupName(String groupName) {
        if (StringUtils.isBlank(groupName)) {
            throw new BusinessException(ErrorCode.GROUP_NAME_EMPTY);
        }
    }

}
