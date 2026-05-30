package com.apiflow.application.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListTaskParam {
    private String taskNo;
    private String apiCode;
    private String status;
    private Integer limit;
}
