package com.apigateway.domain.task.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskQuery {
    private String taskNo;
    private String apiCode;
    private String status;
    private String source;
    private Integer limit;
    private Integer offset;
}
