package com.apiflow.infrastructure.scheduler;

import com.apiflow.domain.scheduler.ApiFlowTaskScheduler;
import com.apiflow.domain.scheduler.TaskChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskChangeEventListener {

    private final ApiFlowTaskScheduler scheduler;

    @EventListener
    public void onTaskChange(TaskChangeEvent event) {
        scheduler.wakeUp();
    }
}
