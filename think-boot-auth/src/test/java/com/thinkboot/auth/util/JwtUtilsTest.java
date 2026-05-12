package com.thinkboot.auth.util;

import com.thinkboot.auth.config.JwtProperties;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JWT 工具测试")
class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String validSecret = "dGhpcy1pcy1hLXNlY3VyZS1qd3Qtc2VjcmV0LWtleS10aGF0LWlzLWxvbmc=";

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(validSecret);
        jwtProperties.setExpiration(7200000L);
        jwtProperties.setRefreshExpiration(604800000L);
        jwtUtils = new JwtUtils(jwtProperties);
    }

    @Test
    @DisplayName("测试生成和解析Token")
    void testGenerateAndParseToken() {
        String userId = "user123";
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "testuser");
        claims.put("role", "USER");

        String token = jwtUtils.generateToken(userId, claims);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        Claims parsedClaims = jwtUtils.parseToken(token);
        assertEquals(userId, parsedClaims.getSubject());
        assertEquals("testuser", parsedClaims.get("username"));
        assertEquals("USER", parsedClaims.get("role"));
    }

    @Test
    @DisplayName("测试生成和解析Refresh Token")
    void testGenerateAndParseRefreshToken() {
        String userId = "user123";
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        String refreshToken = jwtUtils.generateRefreshToken(userId, claims);
        
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        
        Claims parsedClaims = jwtUtils.parseToken(refreshToken);
        assertEquals(userId, parsedClaims.getSubject());
        assertEquals("refresh", parsedClaims.get("type"));
    }

    @Test
    @DisplayName("测试验证有效Token")
    void testValidateValidToken() {
        String token = jwtUtils.generateToken("user123", Map.of("key", "value"));
        
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    @DisplayName("测试验证无效Token")
    void testValidateInvalidToken() {
        String invalidToken = "invalid.token.here";
        
        assertFalse(jwtUtils.validateToken(invalidToken));
    }

    @Test
    @DisplayName("测试验证null Token")
    void testValidateNullToken() {
        assertFalse(jwtUtils.validateToken(null));
    }

    @Test
    @DisplayName("测试验证空Token")
    void testValidateEmptyToken() {
        assertFalse(jwtUtils.validateToken(""));
    }

    @Test
    @DisplayName("测试获取用户ID")
    void testGetUserId() {
        String userId = "user123";
        String token = jwtUtils.generateToken(userId, Map.of());
        
        assertEquals(userId, jwtUtils.getUserId(token));
    }

    @Test
    @DisplayName("测试Token过期检测")
    void testTokenExpiration() {
        String token = jwtUtils.generateToken("user123", Map.of());
        
        assertFalse(jwtUtils.isTokenExpired(token));
    }

    @Test
    @DisplayName("测试过期Token检测")
    void testExpiredToken() {
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNjAwMDAwMDAwLCJleHAiOjE2MDAwMDAwMDF9.abc123";
        
        assertTrue(jwtUtils.isTokenExpired(expiredToken));
    }

    @Test
    @DisplayName("测试获取过期时间")
    void testGetExpiration() {
        assertEquals(7200000L, jwtUtils.getExpiration());
    }

    @Test
    @DisplayName("测试Token携带自定义Claims")
    void testTokenWithMultipleClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1001L);
        claims.put("username", "admin");
        claims.put("email", "admin@example.com");
        claims.put("roles", new String[]{"ADMIN", "USER"});

        String token = jwtUtils.generateToken("1001", claims);
        Claims parsedClaims = jwtUtils.parseToken(token);

        assertEquals("1001", parsedClaims.getSubject());
        assertEquals(1001, parsedClaims.get("userId"));
        assertEquals("admin", parsedClaims.get("username"));
        assertEquals("admin@example.com", parsedClaims.get("email"));
    }

    @Test
    @DisplayName("测试修改Token后验证失败")
    void testModifiedTokenValidation() {
        String token = jwtUtils.generateToken("user123", Map.of("role", "USER"));
        String modifiedToken = token + "modified";
        
        assertFalse(jwtUtils.validateToken(modifiedToken));
    }

    @Test
    @DisplayName("测试使用不同密钥解析失败")
    void testParseWithDifferentSecret() {
        String token = jwtUtils.generateToken("user123", Map.of());
        
        JwtProperties differentProps = new JwtProperties();
        differentProps.setSecret("YW5vdGhlci1zZWNyZXQta2V5LXRoYXQtaXMtYWxzby1sb25nLWVuYXVnaA==");
        differentProps.setExpiration(7200000L);
        differentProps.setRefreshExpiration(604800000L);
        JwtUtils differentUtils = new JwtUtils(differentProps);
        
        assertFalse(differentUtils.validateToken(token));
    }
}
