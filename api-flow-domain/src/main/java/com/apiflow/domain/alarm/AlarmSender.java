package com.apiflow.domain.alarm;

public interface AlarmSender {

    void send(AlarmEvent event);
}
