package com.apiflow.application.config.event;

import com.apiflow.application.configlog.ConfigChangeLogService;
import com.apiflow.common.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigEventHandler {

    private final ConfigChangeLogService configChangeLogService;

    public void handleCreated(ConfigEventMessage msg) {
        saveChangeLog(msg, "CREATE");
    }

    public void handleUpdated(ConfigEventMessage msg) {
        saveChangeLog(msg, "UPDATE");
    }

    public void handleDeleted(ConfigEventMessage msg) {
        saveChangeLog(msg, "DELETE");
    }

    public void handleEnabled(ConfigEventMessage msg) {
        saveChangeLog(msg, "ENABLE");
    }

    public void handleDisabled(ConfigEventMessage msg) {
        saveChangeLog(msg, "DISABLE");
    }

    private void saveChangeLog(ConfigEventMessage msg, String changeType) {
        try {
            configChangeLogService.saveLog(msg.apiCode(), changeType, null, null, msg.operator());
        } catch (Exception e) {
            log.error("Failed to save config change log: apiCode={}, changeType={}", msg.apiCode(), changeType, e);
        }
    }
}
