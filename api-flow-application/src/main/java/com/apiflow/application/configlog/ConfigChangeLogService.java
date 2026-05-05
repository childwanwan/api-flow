package com.apiflow.application.configlog;

import com.apiflow.api.repository.configlog.ConfigChangeLogRepository;
import com.apiflow.api.repository.configlog.idto.ConfigChangeLogIDTO;
import com.apiflow.api.repository.configlog.param.SaveConfigChangeLogParam;
import com.apiflow.api.repository.configlog.param.SelectConfigChangeLogParam;
import com.apiflow.common.repository.FieldCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigChangeLogService {

    private final ConfigChangeLogRepository configChangeLogRepository;

    public ConfigChangeLogIDTO saveLog(String apiCode, String changeType, String beforeConfig, String afterConfig, String operator) {
        SaveConfigChangeLogParam param = SaveConfigChangeLogParam.builder()
                .apiCode(apiCode)
                .changeType(changeType)
                .beforeConfig(beforeConfig)
                .afterConfig(afterConfig)
                .operator(operator)
                .createTimeMs(System.currentTimeMillis())
                .build();
        return configChangeLogRepository.save(param);
    }

    public List<ConfigChangeLogIDTO> listLogs(String apiCode, String changeType, Long startTimeMs, Long endTimeMs, Integer limit) {
        SelectConfigChangeLogParam.SelectConfigChangeLogParamBuilder builder = SelectConfigChangeLogParam.builder();
        if (apiCode != null) {
            builder.apiCode(FieldCondition.of(apiCode));
        }
        if (changeType != null) {
            builder.changeType(FieldCondition.of(changeType));
        }
        if (startTimeMs != null && endTimeMs != null) {
            builder.createTimeMs(FieldCondition.<Long>builder().betweenStart(startTimeMs).betweenEnd(endTimeMs).build());
        }
        if (limit != null) {
            builder.limit(limit);
        }
        return configChangeLogRepository.selectList(builder.build());
    }

    public long count(String apiCode, String changeType) {
        SelectConfigChangeLogParam.SelectConfigChangeLogParamBuilder builder = SelectConfigChangeLogParam.builder();
        if (apiCode != null) {
            builder.apiCode(FieldCondition.of(apiCode));
        }
        if (changeType != null) {
            builder.changeType(FieldCondition.of(changeType));
        }
        return configChangeLogRepository.count(builder.build());
    }
}
