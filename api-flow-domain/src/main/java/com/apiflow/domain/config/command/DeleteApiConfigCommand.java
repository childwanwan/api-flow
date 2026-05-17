package com.apiflow.domain.config.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteApiConfigCommand {

    private final String apiCode;
    private final String operator;
}
