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

    private String jwtSecret;

    @PostConstruct
    public void validate() {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException("Gateway JWT Secret must be configured in application.yml (thinkboot.gateway.jwt-secret). Please generate a secure Base64-encoded 256-bit key.");
        }
        if (!jwtSecret.matches("^[A-Za-z0-9+/=]{32,}$")) {
            throw new IllegalStateException("Gateway JWT Secret must be a valid Base64-encoded key of at least 32 characters. Please generate a secure key and configure it.");
        }
    }
}
