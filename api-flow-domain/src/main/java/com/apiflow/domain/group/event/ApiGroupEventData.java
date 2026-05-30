package com.apiflow.domain.group.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiGroupEventData {
    private final String groupNo;
    private final String groupCode;
    private final String groupName;
    private final String groupDescription;
    private final String operator;
}
