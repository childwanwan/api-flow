package com.apiflow.application.operationlog;

import com.apiflow.api.repository.operationlog.OperationLogRepository;
import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.SaveOperationLogParam;
import com.apiflow.api.repository.operationlog.param.SelectPageOperationLogParam;
import com.apiflow.application.operationlog.converter.OperationLogConverter;
import com.apiflow.application.operationlog.dto.OperationLogDTO;
import com.apiflow.application.operationlog.param.CreateOperationLogParam;
import com.apiflow.application.operationlog.param.PageOperationLogParam;
import com.apiflow.common.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogApplicationService {

    private final static OperationLogConverter CONVERTER = OperationLogConverter.INSTANCE;

    private final OperationLogRepository operationLogRepository;

    public void saveLog(CreateOperationLogParam param) {
        SaveOperationLogParam saveOperationLogParam = CONVERTER.toSaveParam(param);
        operationLogRepository.save(saveOperationLogParam);
    }

    public PageResult<OperationLogDTO> pageLogs(PageOperationLogParam param) {
        SelectPageOperationLogParam queryParam = CONVERTER.pageParam2SelectPageParam(param);
        PageResult<OperationLogIDTO> page = operationLogRepository.selectPage(queryParam);
        return PageResult.of(CONVERTER.toDTOList(page.getRecords()),
                page.getTotal(), param.getEffectiveCurrent(), param.getEffectiveSize());
    }
}
