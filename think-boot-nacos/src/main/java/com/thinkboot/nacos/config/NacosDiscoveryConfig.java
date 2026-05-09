package com.thinkboot.nacos.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "spring.cloud.nacos.discovery.enabled", havingValue = "true", matchIfMissing = true)
public class NacosDiscoveryConfig {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Value("${thinkboot.nacos.version:1.0.0}")
    private String version;

    @Value("${thinkboot.nacos.description:ThinkBoot Application}")
    private String description;

    @Value("${thinkboot.nacos.metadata.region:default}")
    private String region;

    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    public NacosDiscoveryConfig(NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
    }

    @PostConstruct
    public void customizeMetadata() {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        if (metadata == null) {
            metadata = new HashMap<>();
            nacosDiscoveryProperties.setMetadata(metadata);
        }

        metadata.put("version", version);
        metadata.put("description", description);
        metadata.put("region", region);
        metadata.put("management.context-path", "/actuator");

        log.info("Nacos discovery metadata configured for service: {}", applicationName);
    }
}
