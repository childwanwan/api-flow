package com.apiflow.application.group.param;

import com.apiflow.common.dto.BasePageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageApiGroupParam extends BasePageParam {

    private String groupNoLike;

    private String groupCodeLike;

    private String groupNameLike;
}
