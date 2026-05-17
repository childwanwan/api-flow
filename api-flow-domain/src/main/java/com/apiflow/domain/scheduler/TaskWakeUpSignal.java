package com.apiflow.domain.scheduler;

import com.apiflow.api.cache.CacheGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class TaskWakeUpSignal {

    private static final String SIGNAL_KEY = "scheduler:wakeup";
    private static final long SIGNAL_TTL_SECONDS = 60;

    private final CacheGateway cacheGateway;

    public void setSignal() {
        try {
            cacheGateway.set(SIGNAL_KEY, "1", SIGNAL_TTL_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Failed to set wake-up signal", e);
        }
    }

    public boolean checkAndConsume() {
        try {
            Object value = cacheGateway.get(SIGNAL_KEY);
            if (value != null) {
                cacheGateway.delete(SIGNAL_KEY);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to check wake-up signal", e);
            return false;
        }
    }
}