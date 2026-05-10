package com.thinkboot.gateway.filter;

import com.thinkboot.gateway.config.GatewayProperties;
import com.thinkboot.gateway.model.GatewayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 网关限流过滤器
 * 基于 Redis 实现 IP 级别滑动窗口限流
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "thinkboot.gateway", name = "rate-limit-enable", havingValue = "true")
public class RateLimitGlobalFilter implements GlobalFilter, Ordered {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final GatewayProperties gatewayProperties;

    private static final String RATE_LIMIT_KEY_PREFIX = "thinkboot:gateway:rate_limit:";

    public RateLimitGlobalFilter(ReactiveStringRedisTemplate redisTemplate, GatewayProperties gatewayProperties) {
        this.redisTemplate = redisTemplate;
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientIp = getClientIp(exchange);
        String key = RATE_LIMIT_KEY_PREFIX + clientIp;

        return redisTemplate.opsForValue().increment(key)
                .flatMap(count -> {
                    if (count == 1) {
                        return redisTemplate.expire(key, java.time.Duration.ofSeconds(gatewayProperties.getRateLimitWindow()))
                                .then(Mono.just(count));
                    }
                    return Mono.just(count);
                })
                .flatMap(count -> {
                    if (count > gatewayProperties.getRateLimitCount()) {
                        log.warn("[RateLimit] IP {} 触发限流，当前请求数: {}/{}s", clientIp, count, gatewayProperties.getRateLimitWindow());
                        return handleRateLimitResponse(exchange);
                    }
                    return chain.filter(exchange);
                })
                .onErrorResume(e -> {
                    log.error("[RateLimit] Redis 异常，跳过限流", e);
                    return chain.filter(exchange);
                });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    private String getClientIp(ServerWebExchange exchange) {
        String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = exchange.getRequest().getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return exchange.getRequest().getRemoteAddress() != null 
                ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() 
                : "unknown";
    }

    private Mono<Void> handleRateLimitResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        GatewayResponse<Void> result = GatewayResponse.fail(429, "请求过于频繁，请稍后再试");
        byte[] bytes = result.toJson().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
