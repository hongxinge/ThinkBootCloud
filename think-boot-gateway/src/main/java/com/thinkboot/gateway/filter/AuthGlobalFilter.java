package com.thinkboot.gateway.filter;

import com.thinkboot.gateway.config.GatewayProperties;
import com.thinkboot.gateway.model.GatewayResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 网关认证过滤器
 * 
 * 企业级安全架构：
 * - 网关是唯一的外部入口，负责完整的Token验证（存在性、格式、签名、过期）
 * - 验证通过后，注入用户信息到请求头（X-User-Id, X-Username, X-User-Claims）
 * - 同时注入内部签名（X-Gateway-Signature），下游服务据此信任请求
 * - 内部服务间调用通过Feign传递内部标记，下游跳过Token验证但仍检查签名
 * 
 * 安全防护：
 * 1. 防止绕过网关直接调用微服务（验证内部签名）
 * 2. 防止伪造请求头（签名基于时间戳+用户ID+密钥）
 * 3. 防止重放攻击（签名有效期5分钟）
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String AUTH_HEADER = "Authorization";
    
    /**
     * 用户信息请求头（网关验证后注入）
     */
    private static final String USER_ID_HEADER = "X-User-Id";
    
    /**
     * 内部签名请求头（下游服务据此信任请求）
     */
    private static final String GATEWAY_SIGNATURE_HEADER = "X-Gateway-Signature";
    private static final String INTERNAL_CALL_HEADER = "X-Internal-Call";
    
    private static final String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";

    private final GatewayProperties gatewayProperties;
    private final ReactiveStringRedisTemplate redisTemplate;
    private final AntPathMatcher pathMatcher;
    private final ObjectMapper objectMapper;

    public AuthGlobalFilter(GatewayProperties gatewayProperties,
                            ReactiveStringRedisTemplate redisTemplate) {
        this.gatewayProperties = gatewayProperties;
        this.redisTemplate = redisTemplate;
        this.pathMatcher = new AntPathMatcher();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().value();

        // 1. 跳过认证的路径（如登录、注册、API文档）
        if (isSkipAuthPath(requestPath)) {
            return chain.filter(exchange);
        }

        // 2. 提取Token
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            return unauthorizedResponse(exchange, "Missing or invalid authorization header");
        }

        String token = authHeader.substring(TOKEN_PREFIX.length());

        // 3. 完整Token验证（格式、签名、过期）
        if (!validateToken(token)) {
            return unauthorizedResponse(exchange, "Invalid or expired token");
        }

        // 4. 检查Token黑名单（登出场景）
        String tokenBlacklistKey = TOKEN_BLACKLIST_PREFIX + token;
        return redisTemplate.hasKey(tokenBlacklistKey)
                .flatMap(isBlacklisted -> {
                    if (Boolean.TRUE.equals(isBlacklisted)) {
                        return unauthorizedResponse(exchange, "Token has been revoked");
                    }

                    // 5. 解析Token，提取用户信息
                    Claims claims = parseToken(token);
                    String userId = claims.getSubject();
                    
                    // 6. 生成内部签名（防绕过网关、防伪造请求头）
                    String signature = generateGatewaySignature(userId);

                    // 7. 注入用户信息和内部签名
                    ServerWebExchange modifiedExchange = exchange.mutate()
                            .request(exchange.getRequest().mutate()
                                    .headers(h -> {
                                        // 注入用户ID
                                        h.set(USER_ID_HEADER, userId);
                                        // 注入内部签名（下游验证）
                                        h.set(GATEWAY_SIGNATURE_HEADER, signature);
                                        // 标记为网关已验证
                                        h.set("X-Gateway-Verified", "true");
                                    })
                                    .build())
                            .build();

                    return chain.filter(modifiedExchange);
                });
    }

    @Override
    public int getOrder() {
        return -100;
    }

    /**
     * 生成网关内部签名
     * 
     * 签名算法：MD5(userId + timestamp + secret)
     * 有效期：5分钟
     */
    private String generateGatewaySignature(String userId) {
        long timestamp = System.currentTimeMillis();
        String data = userId + ":" + timestamp + ":" + gatewayProperties.getJwtSecret();
        String md5Hash = DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
        return md5Hash + ":" + timestamp;
    }

    private boolean isSkipAuthPath(String requestPath) {
        List<String> skipPaths = gatewayProperties.getSkipAuthPaths();
        if (skipPaths == null || skipPaths.isEmpty()) {
            return false;
        }
        return skipPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }

    private boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private String getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(gatewayProperties.getJwtSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        GatewayResponse<Void> errorResponse = GatewayResponse.error(401, message);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            return response.setComplete();
        }
    }
}
