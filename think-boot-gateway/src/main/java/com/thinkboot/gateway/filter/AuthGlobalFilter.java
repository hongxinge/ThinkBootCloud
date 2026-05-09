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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String AUTH_HEADER = "Authorization";
    private static final String USER_ID_HEADER = "X-User-Id";
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

        if (isSkipAuthPath(requestPath)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            return unauthorizedResponse(exchange, "Missing or invalid authorization header");
        }

        String token = authHeader.substring(TOKEN_PREFIX.length());

        if (!validateToken(token)) {
            return unauthorizedResponse(exchange, "Invalid or expired token");
        }

        String tokenBlacklistKey = TOKEN_BLACKLIST_PREFIX + token;
        return redisTemplate.hasKey(tokenBlacklistKey)
                .flatMap(isBlacklisted -> {
                    if (Boolean.TRUE.equals(isBlacklisted)) {
                        return unauthorizedResponse(exchange, "Token has been revoked");
                    }

                    String userId = getUserId(token);
                    ServerWebExchange modifiedExchange = exchange.mutate()
                            .request(exchange.getRequest().mutate()
                                    .header(USER_ID_HEADER, userId)
                                    .build())
                            .build();

                    return chain.filter(modifiedExchange);
                });
    }

    @Override
    public int getOrder() {
        return -100;
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
