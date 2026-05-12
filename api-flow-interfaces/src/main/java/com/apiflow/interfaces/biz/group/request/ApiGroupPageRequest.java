package com.apiflow.interfaces.biz.group.request;

import com.apiflow.common.dto.BasePageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApiGroupPageRequest extends BasePageParam {

    private String groupNoLike;

    private String groupCodeLike;

    private String groupNameLike;
}
