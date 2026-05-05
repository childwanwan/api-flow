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
public class SelectOneTaskParam {
    private String taskNo;
    private String apiCode;
    private String status;
    private String source;
    private List<TaskField> selectFields;
}
