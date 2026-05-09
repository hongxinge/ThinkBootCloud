package com.thinkboot.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CoreProperties.class)
@ComponentScan(basePackages = "com.thinkboot.core")
public class ThinkBootCoreAutoConfiguration {
}
