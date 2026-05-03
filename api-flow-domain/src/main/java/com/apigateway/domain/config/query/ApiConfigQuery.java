package com.apigateway.domain.config.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiConfigQuery {
    private String apiCode;
    private String groupNo;
    private Boolean enabled;
}
