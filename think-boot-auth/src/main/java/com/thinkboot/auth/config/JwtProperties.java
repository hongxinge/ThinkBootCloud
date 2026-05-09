package com.thinkboot.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "thinkboot.auth.jwt")
public class JwtProperties {

    private String secret = "thinkboot-auth-default-secret-key-must-be-at-least-256-bits";

    private long expiration = 7200000L;

    private long refreshExpiration = 604800000L;

    private List<String> skipPaths;
}
