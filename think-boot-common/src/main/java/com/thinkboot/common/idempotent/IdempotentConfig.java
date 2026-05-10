package com.thinkboot.common.idempotent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(prefix = "thinkboot.core", name = "idempotent-enabled", havingValue = "true", matchIfMissing = true)
public class IdempotentConfig implements WebMvcConfigurer {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public IdempotentConfig(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IdempotentInterceptor(stringRedisTemplate, objectMapper))
                .addPathPatterns("/**");
    }
}
