package com.apigateway.infrastructure.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan(basePackages = "com.apigateway.infrastructure")
@MapperScan("com.apigateway.infrastructure.persistence.mybatis.mapper")
public class TestInfrastructureConfig {
}
