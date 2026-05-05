package com.apiflow.bootstrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.apiflow")
@MapperScan("com.apiflow.infrastructure.persistence.mybatis")
public class ApiFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiFlowApplication.class, args);
    }

}
