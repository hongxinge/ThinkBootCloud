package com.thinkboot.sentinel.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SentinelProperties.class)
@ComponentScan(basePackages = "com.thinkboot.sentinel")
public class ThinkBootSentinelAutoConfiguration {
}
