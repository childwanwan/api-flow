package com.apigateway.application.config.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiConfigDisableCommand {
    private String apiCode;
    private String operator;
}
