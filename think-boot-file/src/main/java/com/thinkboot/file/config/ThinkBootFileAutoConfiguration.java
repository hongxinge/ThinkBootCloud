package com.thinkboot.file.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FileProperties.class)
@ComponentScan(basePackages = "com.thinkboot.file")
public class ThinkBootFileAutoConfiguration {
}