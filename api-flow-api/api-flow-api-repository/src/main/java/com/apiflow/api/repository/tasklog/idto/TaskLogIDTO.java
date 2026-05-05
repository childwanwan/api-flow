package com.apiflow.api.repository.tasklog.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskLogIDTO {
    private Long id;
    private String taskNo;
    private String logType;
    private String logData;
    private Long createTimeMs;
}
