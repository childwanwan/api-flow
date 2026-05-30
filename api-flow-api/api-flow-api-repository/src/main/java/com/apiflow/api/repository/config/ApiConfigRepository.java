package com.apiflow.api.repository.config;

import com.apiflow.api.repository.config.idto.ApiConfigIDTO;
import com.apiflow.api.repository.config.param.SaveApiConfigParam;
import com.apiflow.api.repository.config.param.SelectOneApiConfigParam;
import com.apiflow.api.repository.config.param.SelectApiConfigParam;
import com.apiflow.api.repository.config.param.SelectPageApiConfigParam;
import com.apiflow.api.repository.config.param.UpdateApiConfigParam;
import com.apiflow.common.result.PageResult;

import java.util.List;
import java.util.Map;

public interface ApiConfigRepository {

    ApiConfigIDTO findByApiCode(String apiCode);

    void save(SaveApiConfigParam param);

    void update(UpdateApiConfigParam param);

    void deleteList(List<Long> idList);

    ApiConfigIDTO selectOne(SelectOneApiConfigParam param);

    List<ApiConfigIDTO> selectList(SelectApiConfigParam param);

    PageResult<ApiConfigIDTO> selectPage(SelectPageApiConfigParam param);

    Map<String, Integer> countByGroupNos(List<String> groupNos);

}
