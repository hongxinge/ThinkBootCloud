package com.thinkboot.auth.model;

import lombok.Data;

import java.util.Map;

/**
 * 登录用户信息
 * 
 * 用于存储当前登录用户的完整信息，包括：
 * - 用户ID（从JWT subject中解析）
 * - Token（当前请求的JWT Token）
 * - 过期时间（秒）
 * - 自定义Claims（JWT中存储的额外信息）
 * 
 * 使用示例：
 * LoginUser loginUser = UserContext.getCurrentUser();
 * String userId = loginUser.getUserId();
 * String username = (String) loginUser.getClaim("username");
 */
@Data
public class LoginUser {

    /**
     * 用户ID（JWT subject）
     */
    private String userId;

    /**
     * 当前请求的 Token
     */
    private String token;

    /**
     * Token 过期时间（秒）
     */
    private Long expireTime;

    /**
     * JWT 自定义 Claims
     * 例如：username、role 等
     */
    private Map<String, Object> claims;

    /**
     * 获取指定 Claim 值
     */
    public Object getClaim(String key) {
        return claims != null ? claims.get(key) : null;
    }

    /**
     * 获取指定 Claim 值（带类型转换）
     */
    @SuppressWarnings("unchecked")
    public <T> T getClaim(String key, Class<T> clazz) {
        if (claims == null) {
            return null;
        }
        Object value = claims.get(key);
        if (value == null) {
            return null;
        }
        return (T) value;
    }
}
