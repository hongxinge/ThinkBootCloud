package com.thinkboot.gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "thinkboot.gateway")
public class GatewayProperties {

    private List<String> skipAuthPaths = new ArrayList<>();

    private List<String> corsAllowedOrigins = new ArrayList<>();

    private boolean rateLimitEnable = false;

    private int rateLimitCount = 100;

    private int rateLimitWindow = 60;

    private String jwtSecret = "dGhpbmstYm9vdC1nYXRld2F5LWp3dC1zZWNyZXQta2V5LW11c3QtYmUtYXQtbGVhc3QtMjU2LWJpdHM=";

    @PostConstruct
    public void validate() {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException("Gateway JWT Secret must not be empty. Please configure it in application.yml");
        }
    }
}
