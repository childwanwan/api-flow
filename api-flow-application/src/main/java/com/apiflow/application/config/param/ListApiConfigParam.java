package com.apiflow.application.config.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListApiConfigParam {
    private String groupNo;
    private String apiCode;
    private String apiName;
    private String status;
    private Integer limit;
}
