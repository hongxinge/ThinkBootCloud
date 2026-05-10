package com.thinkboot.gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "thinkboot.gateway")
public class GatewayProperties {

    private static final String DEFAULT_JWT_SECRET = "dGhpbmstYm9vdC1nYXRld2F5LWp3dC1zZWNyZXQta2V5LW11c3QtYmUtYXQtbGVhc3QtMjU2LWJpdHM=";

    private List<String> skipAuthPaths = new ArrayList<>();

    private List<String> corsAllowedOrigins = new ArrayList<>();

    private boolean rateLimitEnable = false;

    private int rateLimitCount = 100;

    private int rateLimitWindow = 60;

    private String jwtSecret = DEFAULT_JWT_SECRET;

    private boolean customJwtSecretConfigured = false;

    @PostConstruct
    public void validate() {
        customJwtSecretConfigured = !DEFAULT_JWT_SECRET.equals(jwtSecret);
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException("Gateway JWT Secret must not be empty.");
        }
        if (!jwtSecret.matches("^[A-Za-z0-9+/=]{32,}$")) {
            throw new IllegalStateException("Gateway JWT Secret must be a valid Base64-encoded key of at least 32 characters.");
        }
        if (!customJwtSecretConfigured) {
            log.warn("===========================================================");
            log.warn("[SECURITY WARNING] Using default JWT secret for Gateway.");
            log.warn("This is acceptable for development/testing only.");
            log.warn("For PRODUCTION, configure a custom secret in application.yml:");
            log.warn("  thinkboot.gateway.jwt-secret: <your-base64-encoded-key>");
            log.warn("===========================================================");
        }
    }
}
