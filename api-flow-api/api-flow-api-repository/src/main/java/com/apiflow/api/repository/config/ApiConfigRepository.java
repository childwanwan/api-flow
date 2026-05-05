package com.apiflow.api.repository.config;

import com.apiflow.api.repository.config.idto.ApiConfigIDTO;
import com.apiflow.api.repository.config.param.SaveApiConfigParam;
import com.apiflow.api.repository.config.param.SelectOneApiConfigParam;
import com.apiflow.api.repository.config.param.SelectApiConfigParam;
import com.apiflow.api.repository.config.param.UpdateApiConfigParam;

import java.util.List;

public interface ApiConfigRepository {

    ApiConfigIDTO save(SaveApiConfigParam param);

    ApiConfigIDTO update(UpdateApiConfigParam param);

    ApiConfigIDTO selectOne(SelectOneApiConfigParam param);

    List<ApiConfigIDTO> selectList(SelectApiConfigParam param);

}
