package com.apiflow.bootstrap.config;

import com.apiflow.api.cache.CacheGateway;
import com.apiflow.api.repository.alarm.AlarmRecordRepository;
import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.api.repository.group.ApiGroupRepository;
import com.apiflow.api.repository.task.TaskRepository;
import com.apiflow.api.repository.tasklog.TaskLogRepository;
import com.apiflow.domain.alarm.AlarmSender;
import com.apiflow.domain.alarm.LogAlarmSender;
import com.apiflow.domain.config.service.ApiConfigDomainService;
import com.apiflow.domain.group.service.ApiGroupDomainService;
import com.apiflow.domain.lock.DistributedLock;
import com.apiflow.domain.plugin.PluginChainExecutor;
import com.apiflow.domain.plugin.builtin.BusinessExecutorPlugin;
import com.apiflow.domain.plugin.builtin.ParamValidatorPlugin;
import com.apiflow.domain.plugin.builtin.RateLimitCheckPlugin;
import com.apiflow.domain.plugin.service.PluginConfigDomainService;
import com.apiflow.api.repository.plugin.PluginConfigRepository;
import com.apiflow.domain.ratelimit.RateLimiter;
import com.apiflow.domain.receipt.HttpReceiptGateway;
import com.apiflow.domain.receipt.ReceiptService;
import com.apiflow.domain.scheduler.ApiFlowTaskScheduler;
import com.apiflow.domain.scheduler.SchedulerLeaderElection;
import com.apiflow.domain.scheduler.TaskWakeUpSignal;
import com.apiflow.domain.task.service.TaskDomainService;
import com.apiflow.api.mq.MessageProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class DomainServiceConfig {

    @Bean
    public ApiConfigDomainService apiConfigDomainService(ApiConfigRepository apiConfigRepository) {
        return new ApiConfigDomainService(apiConfigRepository);
    }

    @Bean
    public TaskDomainService taskDomainService(TaskRepository taskRepository) {
        return new TaskDomainService(taskRepository);
    }

    @Bean
    public ApiGroupDomainService apiGroupDomainService(ApiGroupRepository apiGroupRepository, ApiConfigRepository apiConfigRepository) {
        return new ApiGroupDomainService(apiGroupRepository, apiConfigRepository);
    }

    @Bean
    public PluginConfigDomainService pluginConfigDomainService(PluginConfigRepository pluginConfigRepository) {
        return new PluginConfigDomainService(pluginConfigRepository);
    }

    @Bean
    public PluginChainExecutor pluginChainExecutor() {
        PluginChainExecutor executor = new PluginChainExecutor();
        executor.registerPlugin(paramValidatorPlugin());
        executor.registerPlugin(rateLimitCheckPlugin());
        executor.registerPlugin(businessExecutorPlugin());
        return executor;
    }

    @Bean
    public AlarmSender alarmSender(AlarmRecordRepository alarmRecordRepository) {
        return new LogAlarmSender(alarmRecordRepository);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Bean
    public DistributedLock distributedLock(CacheGateway cacheGateway) {
        return new DistributedLock(cacheGateway);
    }

    @Bean
    public SchedulerLeaderElection schedulerLeaderElection(CacheGateway cacheGateway) {
        return new SchedulerLeaderElection(cacheGateway);
    }

    @Bean
    public TaskWakeUpSignal taskWakeUpSignal(CacheGateway cacheGateway) {
        return new TaskWakeUpSignal(cacheGateway);
    }

    @Bean
    public RateLimiter rateLimiter(CacheGateway cacheGateway) {
        return new RateLimiter(cacheGateway);
    }

    @Bean
    public ReceiptService receiptService(MessageProducer messageProducer, HttpReceiptGateway httpReceiptGateway) {
        return new ReceiptService(messageProducer, httpReceiptGateway);
    }

    @Bean
    public ApiFlowTaskScheduler apiFlowTaskScheduler(
            TaskRepository taskRepository,
            TaskLogRepository taskLogRepository,
            ApiConfigRepository apiConfigRepository,
            DistributedLock distributedLock,
            PluginChainExecutor pluginChainExecutor,
            ReceiptService receiptService,
            AlarmSender alarmSender,
            SchedulerLeaderElection leaderElection,
            TaskWakeUpSignal wakeUpSignal,
            @Qualifier("taskExecutor") ExecutorService taskExecutor,
            @Qualifier("retryScheduler") ScheduledExecutorService retryScheduler,
            @Qualifier("leaderRenewScheduler") ScheduledExecutorService leaderRenewScheduler,
            @Qualifier("signalCheckScheduler") ScheduledExecutorService signalCheckScheduler) {
        return new ApiFlowTaskScheduler(
                taskRepository, taskLogRepository, apiConfigRepository,
                distributedLock, pluginChainExecutor, receiptService,
                alarmSender, leaderElection, wakeUpSignal,
                taskExecutor, retryScheduler, leaderRenewScheduler, signalCheckScheduler);
    }

    @Bean
    public ParamValidatorPlugin paramValidatorPlugin() {
        return new ParamValidatorPlugin();
    }

    @Bean
    public RateLimitCheckPlugin rateLimitCheckPlugin() {
        return new RateLimitCheckPlugin();
    }

    @Bean
    public BusinessExecutorPlugin businessExecutorPlugin() {
        return new BusinessExecutorPlugin();
    }
}
