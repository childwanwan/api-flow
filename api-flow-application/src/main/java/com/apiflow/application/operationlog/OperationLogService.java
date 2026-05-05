package com.apiflow.application.operationlog;

import com.apiflow.api.repository.operationlog.OperationLogRepository;
import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.SaveOperationLogParam;
import com.apiflow.api.repository.operationlog.param.SelectOperationLogParam;
import com.apiflow.common.repository.FieldCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final OperationLogRepository operationLogRepository;

    public void saveLog(String username, String operation, String module, String detail, String ip) {
        try {
            SaveOperationLogParam param = SaveOperationLogParam.builder()
                    .username(username)
                    .operation(operation)
                    .module(module)
                    .detail(detail)
                    .ip(ip)
                    .createTimeMs(System.currentTimeMillis())
                    .build();
            operationLogRepository.save(param);
        } catch (Exception e) {
            log.error("Failed to save operation log", e);
        }
    }

    public List<OperationLogIDTO> listLogs(String username, String operation, String module, Integer limit) {
        SelectOperationLogParam.SelectOperationLogParamBuilder builder = SelectOperationLogParam.builder();
        if (username != null) {
            builder.username(FieldCondition.of(username));
        }
        if (operation != null) {
            builder.operation(FieldCondition.of(operation));
        }
        if (module != null) {
            builder.module(FieldCondition.of(module));
        }
        if (limit != null) {
            builder.limit(limit);
        }
        return operationLogRepository.selectList(builder.build());
    }

    public long count(String username, String operation, String module) {
        SelectOperationLogParam.SelectOperationLogParamBuilder builder = SelectOperationLogParam.builder();
        if (username != null) {
            builder.username(FieldCondition.of(username));
        }
        if (operation != null) {
            builder.operation(FieldCondition.of(operation));
        }
        if (module != null) {
            builder.module(FieldCondition.of(module));
        }
        return operationLogRepository.count(builder.build());
    }
}
