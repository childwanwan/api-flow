package com.apiflow.domain.group.model;

import cn.hutool.core.util.StrUtil;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.util.CodeUtil;
import com.apiflow.domain.group.event.ApiGroupDomainEvent;
import com.apiflow.domain.shared.model.AggregateRoot;
import lombok.Getter;

@Getter
public class ApiGroup extends AggregateRoot {

    private Long id;
    private String groupNo;
    private String groupCode;
    private String groupName;
    private String groupDescription;
    private Long createTimeMs;
    private Long updateTimeMs;
    private String createOperator;
    private String updateOperator;

    private ApiGroup() {
    }

    public static ApiGroup create(String groupCode, String groupName, String groupDescription, String operator) {
        validateGroupCode(groupCode);
        validateGroupName(groupName);
        validateGroupDescription(groupDescription);

        ApiGroup group = new ApiGroup();
        group.groupNo = CodeUtil.generateCode(8);
        group.groupCode = groupCode;
        group.groupName = groupName;
        group.groupDescription = groupDescription;
        long now = System.currentTimeMillis();
        group.createTimeMs = now;
        group.updateTimeMs = now;
        group.createOperator = operator;
        group.updateOperator = operator;

        group.registerEvent(new ApiGroupDomainEvent.Created(
                group.groupNo, groupCode, groupName, groupDescription, operator
        ));

        return group;
    }

    public static ReconstituteBuilder reconstitute() {
        return new ReconstituteBuilder();
    }

    public static class ReconstituteBuilder {
        private final ApiGroup group = new ApiGroup();

        public ReconstituteBuilder id(Long id) { group.id = id; return this; }
        public ReconstituteBuilder groupNo(String groupNo) { group.groupNo = groupNo; return this; }
        public ReconstituteBuilder groupCode(String groupCode) { group.groupCode = groupCode; return this; }
        public ReconstituteBuilder groupName(String groupName) { group.groupName = groupName; return this; }
        public ReconstituteBuilder groupDescription(String groupDescription) { group.groupDescription = groupDescription; return this; }
        public ReconstituteBuilder createTimeMs(Long createTimeMs) { group.createTimeMs = createTimeMs; return this; }
        public ReconstituteBuilder updateTimeMs(Long updateTimeMs) { group.updateTimeMs = updateTimeMs; return this; }
        public ReconstituteBuilder createOperator(String createOperator) { group.createOperator = createOperator; return this; }
        public ReconstituteBuilder updateOperator(String updateOperator) { group.updateOperator = updateOperator; return this; }

        public ApiGroup build() { return group; }
    }

    public void update(String groupCode, String groupName, String groupDescription, String operator) {
        String oldGroupCode = this.groupCode;
        String oldGroupName = this.groupName;
        String oldGroupDescription = this.groupDescription;

        if (StrUtil.isNotBlank(groupCode)) {
            validateGroupCode(groupCode);
            this.groupCode = groupCode;
        }
        if (StrUtil.isNotBlank(groupName)) {
            validateGroupName(groupName);
            this.groupName = groupName;
        }
        if (groupDescription != null) {
            validateGroupDescription(groupDescription);
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
                this.groupNo, operator
        ));
    }

    public boolean isSameGroupCode(String otherGroupCode) {
        return this.groupCode != null && this.groupCode.equals(otherGroupCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiGroup other)) return false;
        return groupNo != null && groupNo.equals(other.groupNo);
    }

    @Override
    public int hashCode() {
        return groupNo != null ? groupNo.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ApiGroup{id=" + id + ", groupNo='" + groupNo + "', groupCode='" + groupCode + "', groupName='" + groupName + "'}";
    }

    private static void validateGroupCode(String groupCode) {
        if (StrUtil.isBlank(groupCode)) {
            throw new BusinessException(ErrorCode.GROUP_CODE_EMPTY);
        }
        if (groupCode.length() > 64) {
            throw new BusinessException(ErrorCode.GROUP_CODE_TOO_LONG);
        }
    }

    private static void validateGroupName(String groupName) {
        if (StrUtil.isBlank(groupName)) {
            throw new BusinessException(ErrorCode.GROUP_NAME_EMPTY);
        }
        if (groupName.length() > 128) {
            throw new BusinessException(ErrorCode.GROUP_NAME_TOO_LONG);
        }
    }

    private static void validateGroupDescription(String groupDescription) {
        if (groupDescription != null && groupDescription.length() > 512) {
            throw new BusinessException(ErrorCode.GROUP_DESCRIPTION_TOO_LONG);
        }
    }
}
