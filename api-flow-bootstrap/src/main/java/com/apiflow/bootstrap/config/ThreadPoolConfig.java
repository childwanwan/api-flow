package com.apiflow.bootstrap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@Configuration
public class ThreadPoolConfig {

    @Bean("taskExecutor")
    public ExecutorService taskExecutor() {
        ThreadFactory factory = Thread.ofVirtual()
                .name("task-executor-", 0)
                .factory();
        return Executors.newThreadPerTaskExecutor(factory);
    }

    @Bean("retryScheduler")
    public ScheduledExecutorService retryScheduler() {
        ThreadFactory factory = r -> {
            Thread t = Thread.ofVirtual()
                    .name("retry-scheduler")
                    .unstarted(r);
            t.setDaemon(true);
            return t;
        };
        return Executors.newSingleThreadScheduledExecutor(factory);
    }

    @Bean("leaderRenewScheduler")
    public ScheduledExecutorService leaderRenewScheduler() {
        ThreadFactory factory = r -> {
            Thread t = Thread.ofVirtual()
                    .name("leader-renew")
                    .unstarted(r);
            t.setDaemon(true);
            return t;
        };
        return Executors.newSingleThreadScheduledExecutor(factory);
    }

    @Bean("signalCheckScheduler")
    public ScheduledExecutorService signalCheckScheduler() {
        ThreadFactory factory = r -> {
            Thread t = Thread.ofVirtual()
                    .name("signal-check")
                    .unstarted(r);
            t.setDaemon(true);
            return t;
        };
        return Executors.newSingleThreadScheduledExecutor(factory);
    }
}
