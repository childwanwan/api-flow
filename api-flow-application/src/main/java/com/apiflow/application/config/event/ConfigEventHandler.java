package com.apiflow.application.config.event;

import com.apiflow.api.cache.CacheGateway;
import com.apiflow.application.configlog.ConfigChangeLogApplicationService;
import com.apiflow.application.configlog.param.SaveConfigChangeLogAppParam;
import com.apiflow.application.operationlog.OperationLogApplicationService;
import com.apiflow.application.operationlog.param.CreateOperationLogParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigEventHandler {

    private static final String IDEMPOTENT_KEY_PREFIX = "config:event:processed:";
    private static final long IDEMPOTENT_KEY_TTL_HOURS = 24;

    private final ConfigChangeLogApplicationService configChangeLogApplicationService;
    private final OperationLogApplicationService operationLogApplicationService;
    private final ObjectMapper objectMapper;
    private final CacheGateway cacheGateway;

    public void handleCreated(ConfigEventMessage msg) {
        if (isDuplicate(msg)) {
            return;
        }
        saveChangeLog(msg, "CREATE");
        saveOperationLog(msg, "CREATE", buildCreateDetail(msg));
    }

    public void handleUpdated(ConfigEventMessage msg) {
        if (isDuplicate(msg)) {
            return;
        }
        saveChangeLog(msg, "UPDATE");
        saveOperationLog(msg, "UPDATE", buildUpdateDetail(msg));
    }

    public void handleDeleted(ConfigEventMessage msg) {
        if (isDuplicate(msg)) {
            return;
        }
        saveChangeLog(msg, "DELETE");
        saveOperationLog(msg, "DELETE", buildDeleteDetail(msg));
    }

    public void handleEnabled(ConfigEventMessage msg) {
        if (isDuplicate(msg)) {
            return;
        }
        saveChangeLog(msg, "ENABLE");
        saveOperationLog(msg, "ENABLE", buildEnableDetail(msg));
    }

    public void handleDisabled(ConfigEventMessage msg) {
        if (isDuplicate(msg)) {
            return;
        }
        saveChangeLog(msg, "DISABLE");
        saveOperationLog(msg, "DISABLE", buildDisableDetail(msg));
    }

    private boolean isDuplicate(ConfigEventMessage msg) {
        try {
            String key = IDEMPOTENT_KEY_PREFIX + msg.header().eventId();
            Boolean absent = cacheGateway.setIfAbsent(key, "1", IDEMPOTENT_KEY_TTL_HOURS, TimeUnit.HOURS);
            if (!Boolean.TRUE.equals(absent)) {
                log.info("Duplicate config event skipped: eventId={}, type={}", msg.header().eventId(), msg.header().eventType());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("Redis idempotent check failed, proceeding without dedup: eventId={}", msg.header().eventId(), e);
            return false;
        }
    }

    private void saveChangeLog(ConfigEventMessage msg, String changeType) {
        try {
            configChangeLogApplicationService.saveLog(SaveConfigChangeLogAppParam.builder()
                    .apiCode(msg.data().apiCode())
                    .changeType(changeType)
                    .beforeConfig(null)
                    .afterConfig(null)
                    .operator(msg.data().operator())
                    .build());
        } catch (Exception e) {
            log.error("Failed to save config change log: apiCode={}, changeType={}", msg.data().apiCode(), changeType, e);
        }
    }

    private void saveOperationLog(ConfigEventMessage msg, String operation, String detail) {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("showDetail", detail);
            logData.put("eventId", msg.header().eventId());
            CreateOperationLogParam param = CreateOperationLogParam.builder()
                    .bizCode(msg.header().aggregateId())
                    .logType(operation)
                    .operator(msg.data().operator())
                    .operateTimeMs(msg.header().occurredOnMs())
                    .logData(objectMapper.writeValueAsString(logData))
                    .build();
            operationLogApplicationService.saveLog(param);
        } catch (Exception e) {
            log.error("Failed to save operation log for config event: type={}, aggregateId={}",
                    msg.header().eventType(), msg.header().aggregateId(), e);
        }
    }

    private String buildCreateDetail(ConfigEventMessage msg) {
        return String.format("创建API[%s], 名称=%s", msg.data().apiCode(), msg.data().apiName());
    }

    private String buildUpdateDetail(ConfigEventMessage msg) {
        return String.format("更新API[%s], 名称=%s", msg.data().apiCode(), msg.data().apiName());
    }

    private String buildDeleteDetail(ConfigEventMessage msg) {
        return String.format("删除API[%s]", msg.data().apiCode());
    }

    private String buildEnableDetail(ConfigEventMessage msg) {
        return String.format("启用API[%s]", msg.data().apiCode());
    }

    private String buildDisableDetail(ConfigEventMessage msg) {
        return String.format("禁用API[%s]", msg.data().apiCode());
    }
}
