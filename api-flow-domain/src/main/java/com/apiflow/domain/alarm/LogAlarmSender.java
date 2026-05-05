package com.apiflow.domain.alarm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogAlarmSender implements AlarmSender {

    @Override
    public void send(AlarmEvent event) {
        log.warn("[ALARM] type={}, level={}, message={}, detail={}",
                event.getEventType(), event.getLevel(), event.getMessage(), event.getDetail());
    }
}
