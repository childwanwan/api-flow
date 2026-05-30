package com.apiflow.application.config.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteApiConfigParam {
    private String apiCode;
    private String operator;
}
