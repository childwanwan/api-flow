package com.apigateway.domain.config.query;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigQuery {
    private String apiCode;
    private String groupNo;
    private Boolean enabled;
}
