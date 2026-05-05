package com.apiflow.domain.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmEvent {

    private String eventType;
    private Long eventTime;
    private String level;
    private String message;
    private Object detail;

    public static AlarmEvent taskFailed(String taskNo, String apiCode, String errorMessage) {
        return AlarmEvent.builder()
                .eventType("TASK_FAILED")
                .eventTime(System.currentTimeMillis())
                .level("WARNING")
                .message("任务执行失败")
                .detail(java.util.Map.of(
                        "taskNo", taskNo,
                        "apiCode", apiCode,
                        "errorMessage", errorMessage != null ? errorMessage : ""
                ))
                .build();
    }

    public static AlarmEvent executeTimeout(String taskNo, String apiCode) {
        return AlarmEvent.builder()
                .eventType("EXECUTE_TIMEOUT")
                .eventTime(System.currentTimeMillis())
                .level("WARNING")
                .message("任务执行超时")
                .detail(java.util.Map.of(
                        "taskNo", taskNo,
                        "apiCode", apiCode
                ))
                .build();
    }

    public static AlarmEvent compensateFailed(String taskNo, String apiCode) {
        return AlarmEvent.builder()
                .eventType("COMPENSATE_FAILED")
                .eventTime(System.currentTimeMillis())
                .level("WARNING")
                .message("补偿执行失败")
                .detail(java.util.Map.of(
                        "taskNo", taskNo,
                        "apiCode", apiCode
                ))
                .build();
    }

    public static AlarmEvent rateLimitTriggered(String apiCode, String detail) {
        return AlarmEvent.builder()
                .eventType("RATE_LIMIT_TRIGGERED")
                .eventTime(System.currentTimeMillis())
                .level("INFO")
                .message("限流触发")
                .detail(java.util.Map.of(
                        "apiCode", apiCode,
                        "detail", detail != null ? detail : ""
                ))
                .build();
    }

    public static AlarmEvent systemError(String errorMessage) {
        return AlarmEvent.builder()
                .eventType("SYSTEM_ERROR")
                .eventTime(System.currentTimeMillis())
                .level("ERROR")
                .message("系统异常")
                .detail(java.util.Map.of("errorMessage", errorMessage != null ? errorMessage : ""))
                .build();
    }

    public static AlarmEvent configChanged(String apiCode, String changeType, String operator) {
        return AlarmEvent.builder()
                .eventType("CONFIG_CHANGED")
                .eventTime(System.currentTimeMillis())
                .level("INFO")
                .message("配置变更通知")
                .detail(java.util.Map.of(
                        "apiCode", apiCode,
                        "changeType", changeType,
                        "operator", operator != null ? operator : ""
                ))
                .build();
    }
}
