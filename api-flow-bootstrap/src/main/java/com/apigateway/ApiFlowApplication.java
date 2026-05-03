package com.apigateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.apigateway")
@MapperScan("com.apigateway.infrastructure.persistence.mybatis")
public class ApiFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiFlowApplication.class, args);
    }

}
