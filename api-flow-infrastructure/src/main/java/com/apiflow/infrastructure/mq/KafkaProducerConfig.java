package com.apiflow.infrastructure.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "api-flow.mq.type", havingValue = "kafka")
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory(
            org.springframework.core.env.Environment env) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                env.getProperty("spring.kafka.bootstrap-servers", "localhost:9092"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG,
                env.getProperty("spring.kafka.producer.acks", "all"));
        props.put(ProducerConfig.RETRIES_CONFIG,
                env.getProperty("spring.kafka.producer.retries", Integer.class, 3));
        props.put(ProducerConfig.LINGER_MS_CONFIG,
                env.getProperty("spring.kafka.producer.properties.linger.ms", Long.class, 10L));
        log.info("Kafka ProducerFactory configured with bootstrap-servers: {}",
                props.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
