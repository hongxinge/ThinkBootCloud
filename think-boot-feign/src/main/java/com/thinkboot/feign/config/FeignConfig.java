package com.thinkboot.feign.config;

import feign.Logger;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * OpenFeign 配置
 * 
 * 设计原则（符合 Spring Cloud Alibaba 规范）：
 * - 安全边界在网关，内部服务间通过 Nacos 注册中心形成信任网络
 * - Feign 内部调用传递 X-Internal-Call 标记，告知下游服务跳过 Token 验证
 * - 同时传递 X-User-Id 和 X-Trace-Id，保持用户上下文和链路追踪
 */
@Configuration
@EnableFeignClients(basePackages = "com.thinkboot")
public class FeignConfig {

    /**
     * 内部调用标记头
     * 告知下游服务：这是内部调用，跳过 Token 验证
     */
    private static final String INTERNAL_CALL_HEADER = "X-Internal-Call";

    /**
     * 网关传递的用户 ID 头
     */
    private static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 分布式追踪 ID 头
     */
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                // 标记为内部调用，下游服务跳过 Token 验证
                template.header(INTERNAL_CALL_HEADER, "true");
                
                // 传递用户 ID（网关验证后注入）
                String userId = request.getHeader(USER_ID_HEADER);
                if (userId != null && !userId.isEmpty()) {
                    template.header(USER_ID_HEADER, userId);
                }
                
                // 传递链路追踪 ID
                String traceId = request.getHeader(TRACE_ID_HEADER);
                if (traceId != null && !traceId.isEmpty()) {
                    template.header(TRACE_ID_HEADER, traceId);
                }
            }
        };
    }
}
