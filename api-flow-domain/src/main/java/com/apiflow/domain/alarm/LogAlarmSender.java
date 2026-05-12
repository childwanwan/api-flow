package com.apiflow.domain.alarm;

import com.apiflow.api.repository.alarm.AlarmRecordRepository;
import com.apiflow.api.repository.alarm.param.SaveAlarmRecordParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LogAlarmSender implements AlarmSender {

    private final AlarmRecordRepository alarmRecordRepository;

    @Override
    public void send(AlarmEvent event) {
        log.warn("[ALARM] type={}, level={}, message={}, detail={}",
                event.getEventType(), event.getLevel(), event.getMessage(), event.getDetail());
        try {
            String taskNo = null;
            String apiCode = null;
            if (event.getDetail() instanceof java.util.Map) {
                java.util.Map<?, ?> detailMap = (java.util.Map<?, ?>) event.getDetail();
                taskNo = detailMap.get("taskNo") != null ? detailMap.get("taskNo").toString() : null;
                apiCode = detailMap.get("apiCode") != null ? detailMap.get("apiCode").toString() : null;
            }
            SaveAlarmRecordParam param = SaveAlarmRecordParam.builder()
                    .eventType(event.getEventType())
                    .level(event.getLevel())
                    .message(event.getMessage())
                    .detail(event.getDetail() != null ? event.getDetail().toString() : null)
                    .taskNo(taskNo)
                    .apiCode(apiCode)
                    .createTimeMs(event.getEventTime() != null ? event.getEventTime() : System.currentTimeMillis())
                    .build();
            alarmRecordRepository.save(param);
        } catch (Exception e) {
            log.error("Failed to save alarm record to database", e);
        }
    }
}
