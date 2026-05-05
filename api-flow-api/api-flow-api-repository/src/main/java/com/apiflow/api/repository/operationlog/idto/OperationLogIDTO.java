package com.apiflow.api.repository.operationlog.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogIDTO {
    private Long id;
    private String username;
    private String operation;
    private String module;
    private String detail;
    private String ip;
    private Long createTimeMs;
}
