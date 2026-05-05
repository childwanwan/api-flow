package com.apiflow.api.repository.configlog.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigChangeLogIDTO {
    private Long id;
    private String apiCode;
    private String changeType;
    private String beforeConfig;
    private String afterConfig;
    private String operator;
    private Long createTimeMs;
}
