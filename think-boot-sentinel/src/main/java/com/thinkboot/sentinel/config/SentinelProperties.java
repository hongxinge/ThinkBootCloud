package com.thinkboot.sentinel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "thinkboot.sentinel")
public class SentinelProperties {
    private boolean enabled = true;
    private String dashboard = "localhost:8080";
    private boolean eager = false;
    private Datasource datasource = new Datasource();

    @Data
    public static class Datasource {
        private Nacos nacos = new Nacos();
    }

    @Data
    public static class Nacos {
        private boolean enabled = false;
        private String serverAddr;
        private String namespace;
        private String groupId = "SENTINEL_GROUP";
        private String dataId;
        private String ruleType = "flow";
    }
}
