package com.thinkboot.auth.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "thinkboot.auth.jwt")
public class JwtProperties {

    private static final String DEFAULT_SECRET = "dGhpbmstYm9vdC1hdXRoLWp3dC1zZWNyZXQta2V5LW11c3QtYmUtYXQtbGVhc3QtMjU2LWJpdHM=";

    private String secret = DEFAULT_SECRET;

    private long expiration = 7200000L;

    private long refreshExpiration = 604800000L;

    private List<String> skipPaths;

    private boolean customSecretConfigured = false;

    @PostConstruct
    public void validate() {
        customSecretConfigured = !DEFAULT_SECRET.equals(secret);
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalStateException("JWT Secret must not be empty.");
        }
        if (!secret.matches("^[A-Za-z0-9+/=]{32,}$")) {
            throw new IllegalStateException("JWT Secret must be a valid Base64-encoded key of at least 32 characters.");
        }
        if (!customSecretConfigured) {
            log.warn("===========================================================");
            log.warn("[SECURITY WARNING] Using default JWT secret.");
            log.warn("This is acceptable for development/testing only.");
            log.warn("For PRODUCTION, configure a custom secret in application.yml:");
            log.warn("  thinkboot.auth.jwt.secret: <your-base64-encoded-key>");
            log.warn("Generate key: SecureUtil.generateKey(\"HmacSHA256\") -> Base64");
            log.warn("===========================================================");
        }
    }
}
