package com.apiflow.api.repository.group;

import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.*;
import com.apiflow.common.result.PageResult;

import java.util.List;

public interface ApiGroupRepository {

    ApiGroupIDTO findByGroupCode(String groupCode);

    ApiGroupIDTO findByGroupNo(String groupNo);

    void save(SaveApiGroupParam param);

    void update(UpdateApiGroupParam param);

    ApiGroupIDTO selectOne(SelectOneApiGroupParam param);

    List<ApiGroupIDTO> selectList(SelectApiGroupParam param);

    PageResult<ApiGroupIDTO> selectPage(SelectPageApiGroupParam param);

    void deleteList(List<Long> idList);
}
