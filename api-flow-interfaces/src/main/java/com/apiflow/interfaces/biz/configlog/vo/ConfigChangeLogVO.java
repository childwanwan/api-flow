package com.apiflow.interfaces.biz.configlog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigChangeLogVO {
    private Long id;
    private String apiCode;
    private String changeType;
    private String beforeConfig;
    private String afterConfig;
    private String operator;
    private Long createTimeMs;
}
