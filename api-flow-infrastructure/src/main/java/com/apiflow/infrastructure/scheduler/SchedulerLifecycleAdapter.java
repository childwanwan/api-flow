package com.apiflow.infrastructure.scheduler;

import com.apiflow.domain.scheduler.ApiFlowTaskScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerLifecycleAdapter implements SmartLifecycle {

    private final ApiFlowTaskScheduler scheduler;

    @Override
    public void start() {
        scheduler.start();
    }

    @Override
    public void stop() {
        scheduler.stop();
    }

    @Override
    public boolean isRunning() {
        return scheduler.isRunning();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE - 1;
    }
}
