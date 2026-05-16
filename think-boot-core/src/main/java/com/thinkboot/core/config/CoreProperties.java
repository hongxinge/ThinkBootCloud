package com.thinkboot.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "thinkboot.core")
public class CoreProperties {
    private boolean enableCors = true;
    private List<String> corsAllowedOrigins = new ArrayList<>();
    private boolean xssFilterEnabled = false;
    private int maxUploadSize = 10;
    private boolean enableTraceId = true;
    private boolean idempotentEnabled = true;
}
