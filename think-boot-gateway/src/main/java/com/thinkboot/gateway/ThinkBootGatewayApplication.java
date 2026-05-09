package com.thinkboot.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ThinkBootGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThinkBootGatewayApplication.class, args);
    }
}
