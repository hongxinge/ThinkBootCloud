package com.thinkboot.auth.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "thinkboot.auth.jwt")
public class JwtProperties {

    private String secret;

    private long expiration = 7200000L;

    private long refreshExpiration = 604800000L;

    private List<String> skipPaths;

    @PostConstruct
    public void validate() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalStateException("JWT Secret must be configured in application.yml (thinkboot.auth.jwt.secret). Please generate a secure Base64-encoded 256-bit key.");
        }
        if (!secret.matches("^[A-Za-z0-9+/=]{32,}$")) {
            throw new IllegalStateException("JWT Secret must be a valid Base64-encoded key of at least 32 characters. Please generate a secure key and configure it.");
        }
    }
}
