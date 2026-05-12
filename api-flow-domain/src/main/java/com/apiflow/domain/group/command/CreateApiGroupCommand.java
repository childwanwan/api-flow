package com.apiflow.domain.group.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateApiGroupCommand {

    private final String groupCode;
    private final String groupName;
    private final String groupDescription;
    private final String operator;
}
