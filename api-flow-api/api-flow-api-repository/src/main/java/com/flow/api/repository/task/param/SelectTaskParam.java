package com.flow.api.repository.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectTaskParam {
    private String taskNo;
    private String apiCode;
    private String status;
    private String source;
    private Integer limit;
    private Integer offset;
    private List<TaskField> selectFields;
}
