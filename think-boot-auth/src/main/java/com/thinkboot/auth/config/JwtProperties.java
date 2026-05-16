package com.thinkboot.auth.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * JWT 认证配置
 * 
 * 设计说明：
 * - 网关是唯一的安全验证点，负责验证外部请求的 Token
 * - 微服务内部调用（OpenFeign）走 Nacos 注册中心的信任网络，不需要再次验证 Token
 * - 微服务拦截器会识别内部调用标记，跳过 JWT 验证
 */
@Data
@ConfigurationProperties(prefix = "thinkboot.auth.jwt")
public class JwtProperties {

    /**
     * JWT 密钥（Base64 编码）
     */
    private String secret;

    /**
     * Token 过期时间（毫秒）
     * 默认 2 小时 = 7200000 毫秒
     */
    private long expiration = 7200000L;

    /**
     * Refresh Token 过期时间（毫秒）
     * 默认 7 天 = 604800000 毫秒
     */
    private long refreshExpiration = 604800000L;

    /**
     * 跳过认证的请求路径列表
     * 支持 Ant 风格通配符，如 /api/public/**
     */
    private List<String> skipPaths;

    /**
     * 自定义 Token Header 名称
     * 默认使用 OAuth 2.0 标准的 Authorization: Bearer <token> 格式
     * 如需自定义，可配置为 X-Token 等其他名称
     */
    private String tokenHeader = "Authorization";

    /**
     * Token 前缀
     * 默认 "Bearer "，配合 Authorization 头使用
     */
    private String tokenPrefix = "Bearer ";

    @PostConstruct
    public void validate() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalStateException("JWT Secret must not be empty. Configure it via environment variable JWT_SECRET or in application.yml");
        }
        if (!secret.matches("^[A-Za-z0-9+/=]{32,}$")) {
            throw new IllegalStateException("JWT Secret must be a valid Base64-encoded key of at least 32 characters");
        }
    }
}
