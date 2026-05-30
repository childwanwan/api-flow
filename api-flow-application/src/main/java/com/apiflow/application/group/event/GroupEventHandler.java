package com.apiflow.application.group.event;

import com.apiflow.api.cache.CacheGateway;
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
public class GroupEventHandler {

    private static final String IDEMPOTENT_KEY_PREFIX = "group:event:processed:";
    private static final long IDEMPOTENT_KEY_TTL_HOURS = 24;

    private final OperationLogApplicationService operationLogApplicationService;
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
            String key = IDEMPOTENT_KEY_PREFIX + msg.header().eventId();
            Boolean absent = cacheGateway.setIfAbsent(key, "1", IDEMPOTENT_KEY_TTL_HOURS, TimeUnit.HOURS);
            if (!Boolean.TRUE.equals(absent)) {
                log.info("Duplicate group event skipped: eventId={}, type={}", msg.header().eventId(), msg.header().eventType());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("Redis idempotent check failed, proceeding without dedup: eventId={}", msg.header().eventId(), e);
            return false;
        }
    }

    private void saveOperationLog(GroupEventMessage msg, String operation, String detail) {
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
            log.error("Failed to save operation log for group event: type={}, aggregateId={}",
                    msg.header().eventType(), msg.header().aggregateId(), e);
        }
    }

    private String buildCreateDetail(GroupEventMessage msg) {
        return String.format("创建分组[%s], 编码=%s, 名称=%s",
                msg.header().aggregateId(), msg.data().groupCode(), msg.data().groupName());
    }

    private String buildUpdateDetail(GroupEventMessage msg) {
        GroupEventMessage.Snapshot before = msg.snapshot();
        StringBuilder sb = new StringBuilder();
        sb.append("更新分组[").append(msg.header().aggregateId()).append("]");
        if (before != null && !nullSafeEquals(before.groupCode(), msg.data().groupCode())) {
            sb.append("\n编码: ").append(before.groupCode()).append(" → ").append(msg.data().groupCode());
        }
        if (before != null && !nullSafeEquals(before.groupName(), msg.data().groupName())) {
            sb.append("\n名称: ").append(before.groupName()).append(" → ").append(msg.data().groupName());
        }
        if (before != null && !nullSafeEquals(before.groupDescription(), msg.data().groupDescription())) {
            sb.append("\n描述: ").append(before.groupDescription()).append(" → ").append(msg.data().groupDescription());
        }
        return sb.toString();
    }

    private boolean nullSafeEquals(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }

    private String buildDeleteDetail(GroupEventMessage msg) {
        return String.format("删除分组[%s]", msg.header().aggregateId());
    }
}
