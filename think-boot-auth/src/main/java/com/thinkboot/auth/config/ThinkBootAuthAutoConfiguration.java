package com.thinkboot.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.thinkboot.auth")
@EnableConfigurationProperties(JwtProperties.class)
public class ThinkBootAuthAutoConfiguration {
}
