package com.apiflow.application.group.event;

import com.apiflow.api.cache.CacheGateway;
import com.apiflow.application.operationlog.OperationLogService;
import com.apiflow.application.operationlog.param.OperationLogCreateParam;
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
public class GroupEventHandler {

    private static final String IDEMPOTENT_KEY_PREFIX = "group:event:processed:";
    private static final long IDEMPOTENT_KEY_TTL_HOURS = 24;

    private final OperationLogService operationLogService;
    private final ObjectMapper objectMapper;
    private final CacheGateway cacheGateway;

    public void handleCreated(GroupEventMessage msg) {
        if (isDuplicate(msg)) {
            return;
        }
        saveOperationLog(msg, "CREATE", buildCreateDetail(msg));
    }

    public void handleUpdated(GroupEventMessage msg) {
        if (isDuplicate(msg)) {
            return;
        }
        saveOperationLog(msg, "UPDATE", buildUpdateDetail(msg));
    }

    public void handleDeleted(GroupEventMessage msg) {
        if (isDuplicate(msg)) {
            return;
        }
        saveOperationLog(msg, "DELETE", buildDeleteDetail(msg));
    }

    private boolean isDuplicate(GroupEventMessage msg) {
        try {
            String key = IDEMPOTENT_KEY_PREFIX + msg.eventId();
            Boolean absent = cacheGateway.setIfAbsent(key, "1", IDEMPOTENT_KEY_TTL_HOURS, TimeUnit.HOURS);
            if (!Boolean.TRUE.equals(absent)) {
                log.info("Duplicate group event skipped: eventId={}, type={}", msg.eventId(), msg.eventType());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("Redis idempotent check failed, proceeding without dedup: eventId={}", msg.eventId(), e);
            return false;
        }
    }

    private void saveOperationLog(GroupEventMessage msg, String operation, String detail) {
        try {
            OperationLogCreateParam param = new OperationLogCreateParam();
            param.setBizCode(msg.aggregateId());
            param.setLogType(operation);
            param.setOperator(msg.operator());
            param.setOperateTimeMs(msg.occurredOnMs());
            Map<String, Object> logData = new HashMap<>();
            logData.put("showDetail", detail);
            logData.put("eventId", msg.eventId());
            param.setLogData(objectMapper.writeValueAsString(logData));
            operationLogService.saveLog(param);
        } catch (Exception e) {
            log.error("Failed to save operation log for group event: type={}, aggregateId={}",
                    msg.eventType(), msg.aggregateId(), e);
        }
    }

    private String buildCreateDetail(GroupEventMessage msg) {
        return String.format("创建分组[%s], 编码=%s, 名称=%s",
                msg.aggregateId(), msg.groupCode(), msg.groupName());
    }

    private String buildUpdateDetail(GroupEventMessage msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("更新分组[").append(msg.aggregateId()).append("]");
        if (msg.oldGroupCode() != null && !msg.oldGroupCode().equals(msg.groupCode())) {
            sb.append("\n编码: ").append(msg.oldGroupCode()).append(" → ").append(msg.groupCode());
        }
        if (msg.oldGroupName() != null && !msg.oldGroupName().equals(msg.groupName())) {
            sb.append("\n名称: ").append(msg.oldGroupName()).append(" → ").append(msg.groupName());
        }
        if (msg.oldGroupDescription() != null && !msg.oldGroupDescription().equals(msg.groupDescription())) {
            sb.append("\n描述: ").append(msg.oldGroupDescription()).append(" → ").append(msg.groupDescription());
        }
        return sb.toString();
    }

    private String buildDeleteDetail(GroupEventMessage msg) {
        return String.format("删除分组[%s], 编码=%s, 名称=%s",
                msg.aggregateId(), msg.groupCode(), msg.groupName());
    }
}
