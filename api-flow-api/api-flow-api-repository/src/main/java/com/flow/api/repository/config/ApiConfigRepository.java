package com.flow.api.repository.config;

import com.flow.api.repository.config.dto.ApiConfigIDTO;
import com.flow.api.repository.config.param.SaveApiConfigParam;
import com.flow.api.repository.config.param.SelectOneApiConfigParam;
import com.flow.api.repository.config.param.SelectApiConfigParam;
import com.flow.api.repository.config.param.UpdateApiConfigParam;

import java.util.List;

public interface ApiConfigRepository {

    ApiConfigIDTO save(SaveApiConfigParam param);

    ApiConfigIDTO update(UpdateApiConfigParam param);

    ApiConfigIDTO selectOne(SelectOneApiConfigParam param);

    List<ApiConfigIDTO> selectList(SelectApiConfigParam param);

}
