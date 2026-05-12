package com.apiflow.bootstrap.config;

import com.apiflow.api.repository.alarm.AlarmRecordRepository;
import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.api.repository.group.ApiGroupRepository;
import com.apiflow.api.repository.task.TaskRepository;
import com.apiflow.domain.alarm.AlarmSender;
import com.apiflow.domain.alarm.LogAlarmSender;
import com.apiflow.domain.config.service.ApiConfigDomainService;
import com.apiflow.domain.group.service.ApiGroupDomainService;
import com.apiflow.domain.plugin.Plugin;
import com.apiflow.domain.plugin.PluginChainExecutor;
import com.apiflow.domain.task.service.TaskDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
    public ApiGroupDomainService apiGroupDomainService(ApiGroupRepository apiGroupRepository) {
        return new ApiGroupDomainService(apiGroupRepository);
    }

    @Bean
    public PluginChainExecutor pluginChainExecutor(List<Plugin> plugins) {
        PluginChainExecutor executor = new PluginChainExecutor();
        for (Plugin plugin : plugins) {
            executor.registerPlugin(plugin);
        }
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
}
