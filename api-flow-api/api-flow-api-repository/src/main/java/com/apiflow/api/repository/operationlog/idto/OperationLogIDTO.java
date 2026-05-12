package com.apiflow.api.repository.operationlog.idto;

import com.apiflow.common.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.core.type.TypeReference;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogIDTO {
    private Long id;
    private String bizCode;
    private String logType;
    private String logData;
    private String operator;
    private Long operateTimeMs;
    private Long createTimeMs;

    private volatile Map<String, Object> logDataMap;

    private Map<String, Object> getLogDataMap() {
        if (logDataMap == null && logData != null) {
            synchronized (this) {
                if (logDataMap == null) {
                    try {
                        logDataMap = JsonUtil.toObject(logData, new TypeReference<Map<String, Object>>() {});
                    } catch (Exception e) {
                        logDataMap = Map.of();
                    }
                }
            }
        }
        return logDataMap;
    }

    public String getShowDetail() {
        Map<String, Object> map = getLogDataMap();
        if (map == null) {
            return null;
        }
        Object val = map.get("showDetail");
        return val != null ? val.toString() : null;
    }
}
