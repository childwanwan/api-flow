package com.apiflow.api.repository.operationlog;

import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.SaveOperationLogParam;
import com.apiflow.api.repository.operationlog.param.SelectOperationLogParam;
import com.apiflow.api.repository.operationlog.param.SelectPageOperationLogParam;
import com.apiflow.common.result.PageResult;

import java.util.List;

public interface OperationLogRepository {

    OperationLogIDTO save(SaveOperationLogParam param);

    List<OperationLogIDTO> selectList(SelectOperationLogParam param);

    long count(SelectOperationLogParam param);

    PageResult<OperationLogIDTO> selectPage(SelectPageOperationLogParam param);
}
