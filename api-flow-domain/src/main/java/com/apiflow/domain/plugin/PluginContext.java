package com.apiflow.domain.plugin;

import com.apiflow.domain.config.model.ExtraConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginContext {

    private String taskNo;
    private String apiCode;
    private String apiName;
    private Map<String, Object> params;
    private Map<String, Object> customData;
    private Map<String, Object> compensateData;
    private ExtraConfig extraConfig;
    private volatile boolean interrupted;

    public static PluginContext of(String taskNo, String apiCode, String apiName,
                                   Map<String, Object> params, Map<String, Object> customData) {
        return PluginContext.builder()
                .taskNo(taskNo)
                .apiCode(apiCode)
                .apiName(apiName)
                .params(params != null ? params : new ConcurrentHashMap<>())
                .customData(customData != null ? customData : new ConcurrentHashMap<>())
                .compensateData(new ConcurrentHashMap<>())
                .interrupted(false)
                .build();
    }

    public static PluginContext of(String taskNo, String apiCode, String apiName,
                                   Map<String, Object> params, Map<String, Object> customData,
                                   ExtraConfig extraConfig) {
        return PluginContext.builder()
                .taskNo(taskNo)
                .apiCode(apiCode)
                .apiName(apiName)
                .params(params != null ? params : new ConcurrentHashMap<>())
                .customData(customData != null ? customData : new ConcurrentHashMap<>())
                .compensateData(new ConcurrentHashMap<>())
                .extraConfig(extraConfig)
                .interrupted(false)
                .build();
    }

    public void putCompensateData(String key, Object value) {
        if (compensateData == null) {
            compensateData = new ConcurrentHashMap<>();
        }
        compensateData.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getCompensateData(String key) {
        if (compensateData == null) return null;
        return (T) compensateData.get(key);
    }

    public void interrupt() {
        this.interrupted = true;
    }

    public boolean isInterrupted() {
        return interrupted;
    }
}
