package com.apiflow.api.repository.group;

import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.SaveApiGroupParam;
import com.apiflow.api.repository.group.param.SelectApiGroupParam;
import com.apiflow.api.repository.group.param.SelectOneApiGroupParam;
import com.apiflow.api.repository.group.param.UpdateApiGroupParam;

import java.util.List;

public interface ApiGroupRepository {

    ApiGroupIDTO save(SaveApiGroupParam param);

    ApiGroupIDTO update(UpdateApiGroupParam param);

    ApiGroupIDTO selectOne(SelectOneApiGroupParam param);

    List<ApiGroupIDTO> selectList(SelectApiGroupParam param);

    long count(SelectApiGroupParam param);
}
