package com.thinkboot.gateway.config;

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

    private String jwtSecret;
}
