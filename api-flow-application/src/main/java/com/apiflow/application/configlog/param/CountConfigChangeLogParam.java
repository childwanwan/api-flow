package com.apiflow.application.configlog.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountConfigChangeLogParam {
    private String apiCode;
    private String changeType;
}
