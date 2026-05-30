package com.apiflow.api.repository.configlog;

import com.apiflow.api.repository.configlog.idto.ConfigChangeLogIDTO;
import com.apiflow.api.repository.configlog.param.SaveConfigChangeLogParam;
import com.apiflow.api.repository.configlog.param.SelectConfigChangeLogParam;

import java.util.List;

public interface ConfigChangeLogRepository {

    void save(SaveConfigChangeLogParam param);

    List<ConfigChangeLogIDTO> selectList(SelectConfigChangeLogParam param);

    long count(SelectConfigChangeLogParam param);
}
