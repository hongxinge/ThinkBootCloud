package com.thinkboot.mybatis.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.datasource.druid", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DruidConfig {
}
