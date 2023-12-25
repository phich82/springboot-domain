package com.sonha.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.sonha.order.service.database", "com.sonha.database"})
@EntityScan(basePackages = {"com.sonha.order.service.database", "com.sonha.database"})
@SpringBootApplication(scanBasePackages = "com.sonha")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
