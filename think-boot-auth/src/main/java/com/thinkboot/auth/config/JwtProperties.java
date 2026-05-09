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

    private String secret = "dGhpbmstYm9vdC1hdXRoLWp3dC1zZWNyZXQta2V5LW11c3QtYmUtYXQtbGVhc3QtMjU2LWJpdHM=";

    private long expiration = 7200000L;

    private long refreshExpiration = 604800000L;

    private List<String> skipPaths;

    @PostConstruct
    public void validate() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalStateException("JWT Secret must not be empty. Please configure it in application.yml");
        }
    }
}
