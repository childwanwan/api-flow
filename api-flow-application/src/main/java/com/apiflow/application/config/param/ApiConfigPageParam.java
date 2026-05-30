package com.apiflow.application.config.param;

import com.apiflow.common.dto.BasePageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigPageParam extends BasePageParam {
    private String groupNo;
    private String groupNoLike;
    private String apiCodeLike;
    private String apiNameLike;
    private String status;
}
