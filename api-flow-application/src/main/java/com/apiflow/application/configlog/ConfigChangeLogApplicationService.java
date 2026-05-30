package com.apiflow.application.configlog;

import com.apiflow.api.repository.configlog.ConfigChangeLogRepository;
import com.apiflow.api.repository.configlog.idto.ConfigChangeLogIDTO;
import com.apiflow.api.repository.configlog.param.SaveConfigChangeLogParam;
import com.apiflow.api.repository.configlog.param.SelectConfigChangeLogParam;
import com.apiflow.application.configlog.param.CountConfigChangeLogParam;
import com.apiflow.application.configlog.param.ListConfigChangeLogParam;
import com.apiflow.application.configlog.param.SaveConfigChangeLogAppParam;
import com.apiflow.common.repository.FieldCondition;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigChangeLogApplicationService {

    private final ConfigChangeLogRepository configChangeLogRepository;

    public void saveLog(SaveConfigChangeLogAppParam param) {
        SaveConfigChangeLogParam saveParam = SaveConfigChangeLogParam.builder()
                .apiCode(param.getApiCode())
                .changeType(param.getChangeType())
                .beforeConfig(param.getBeforeConfig())
                .afterConfig(param.getAfterConfig())
                .operator(param.getOperator())
                .createTimeMs(System.currentTimeMillis())
                .build();
        configChangeLogRepository.save(saveParam);
    }

    public List<ConfigChangeLogIDTO> listLogs(ListConfigChangeLogParam param) {
        SelectConfigChangeLogParam.SelectConfigChangeLogParamBuilder builder = SelectConfigChangeLogParam.builder();
        if (StrUtil.isNotEmpty(param.getApiCode())) {
            builder.apiCode(FieldCondition.of(param.getApiCode()));
        }
        if (StrUtil.isNotEmpty(param.getChangeType())) {
            builder.changeType(FieldCondition.of(param.getChangeType()));
        }
        if (param.getStartTimeMs() != null && param.getEndTimeMs() != null) {
            builder.createTimeMs(FieldCondition.<Long>builder().betweenStart(param.getStartTimeMs()).betweenEnd(param.getEndTimeMs()).build());
        }
        if (param.getLimit() != null) {
            builder.limit(param.getLimit());
        }
        return configChangeLogRepository.selectList(builder.build());
    }

    public long count(CountConfigChangeLogParam param) {
        SelectConfigChangeLogParam.SelectConfigChangeLogParamBuilder builder = SelectConfigChangeLogParam.builder();
        if (StrUtil.isNotEmpty(param.getApiCode())) {
            builder.apiCode(FieldCondition.of(param.getApiCode()));
        }
        if (StrUtil.isNotEmpty(param.getChangeType())) {
            builder.changeType(FieldCondition.of(param.getChangeType()));
        }
        return configChangeLogRepository.count(builder.build());
    }
}
