package com.apiflow.domain.group.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteApiGroupCommand {

    private final String groupNo;
    private final String operator;
}
