package com.example.camel.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.camel.repository")
@EntityScan(basePackages = "com.example.camel.domain")
public class CamelProducer {
    public static void main(String[] args) {
        SpringApplication.run(CamelProducer.class, args);
    }
}
