package com.apiflow.application.group.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteApiGroupParam {

    private String groupNo;

    private String operator;
}
