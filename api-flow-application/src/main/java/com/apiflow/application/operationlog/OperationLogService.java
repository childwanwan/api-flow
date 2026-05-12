package com.apiflow.application.operationlog;

import com.apiflow.api.repository.operationlog.OperationLogRepository;
import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.SaveOperationLogParam;
import com.apiflow.api.repository.operationlog.param.SelectPageOperationLogParam;
import com.apiflow.application.operationlog.converter.OperationLogConverter;
import com.apiflow.application.operationlog.dto.OperationLogDTO;
import com.apiflow.application.operationlog.param.OperationLogCreateParam;
import com.apiflow.application.operationlog.param.OperationLogPageParam;
import com.apiflow.common.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final static OperationLogConverter CONVERTER = OperationLogConverter.INSTANCE;

    private final OperationLogRepository operationLogRepository;

    public void saveLog(OperationLogCreateParam param) {
        SaveOperationLogParam saveOperationLogParam = CONVERTER.operationLogCreateParam2SaveOperationLogParam(param);
        operationLogRepository.save(saveOperationLogParam);
    }

    public void saveLog(String bizCode, String logType, String module, String detail, String ip) {
        OperationLogCreateParam param = new OperationLogCreateParam();
        param.setBizCode(bizCode);
        param.setLogType(logType);
        param.setLogData(detail);
        param.setOperator(null);
        param.setOperateTimeMs(System.currentTimeMillis());
        param.setCreateTimeMs(System.currentTimeMillis());
        saveLog(param);
    }

    public PageResult<OperationLogDTO> pageLogs(OperationLogPageParam param) {
        SelectPageOperationLogParam queryParam = OperationLogConverter.INSTANCE.operationLogPageParam2SelectPageOperationLogParam(param);
        PageResult<OperationLogIDTO> page = operationLogRepository.selectPage(queryParam);
        return PageResult.of(OperationLogConverter.INSTANCE.operationLogIDTO2OperationLogDTOList(page.getRecords()),
                page.getTotal(), param.getEffectiveCurrent(), param.getEffectiveSize());
    }
}
