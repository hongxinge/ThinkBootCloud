package com.thinkboot.auth.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JWT 配置测试")
class JwtPropertiesTest {

    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
    }

    @Test
    @DisplayName("测试默认值")
    void testDefaultValues() {
        assertEquals(7200000L, jwtProperties.getExpiration(), "Token expiration should be 2 hours by default (7200000ms)");
        assertEquals(604800000L, jwtProperties.getRefreshExpiration(), "Refresh token expiration should be 7 days by default (604800000ms)");
        assertNull(jwtProperties.getSecret(), "Secret should be null by default");
        assertNull(jwtProperties.getSkipPaths(), "Skip paths should be null by default");
    }

    @Test
    @DisplayName("测试设置有效密钥")
    void testSetValidSecret() {
        String validSecret = "dGhpcy1pcy1hLXNlY3VyZS1qd3Qtc2VjcmV0LWtleS10aGF0LWlzLWxvbmc=";
        jwtProperties.setSecret(validSecret);
        assertEquals(validSecret, jwtProperties.getSecret());
        
        assertDoesNotThrow(() -> jwtProperties.validate(), "Validation should not throw with valid secret");
    }

    @Test
    @DisplayName("测试空密钥验证失败")
    void testEmptySecretValidation() {
        jwtProperties.setSecret("");
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> jwtProperties.validate());
        assertTrue(exception.getMessage().contains("JWT Secret must not be empty"));
    }

    @Test
    @DisplayName("测试 null 密钥验证失败")
    void testNullSecretValidation() {
        jwtProperties.setSecret(null);
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> jwtProperties.validate());
        assertTrue(exception.getMessage().contains("JWT Secret must not be empty"));
    }

    @Test
    @DisplayName("测试过短密钥验证失败")
    void testShortSecretValidation() {
        jwtProperties.setSecret("dGhpcy1pcy1zaG9ydA==");
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> jwtProperties.validate());
        assertTrue(exception.getMessage().contains("JWT Secret must be a valid Base64-encoded key"));
    }

    @Test
    @DisplayName("测试无效 Base64 密钥验证失败")
    void testInvalidBase64SecretValidation() {
        jwtProperties.setSecret("not-base64-encoded!@#$%^&*()");
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> jwtProperties.validate());
        assertTrue(exception.getMessage().contains("JWT Secret must be a valid Base64-encoded key"));
    }

    @Test
    @DisplayName("测试设置跳过路径")
    void testSetSkipPaths() {
        List<String> paths = List.of("/api/public/**", "/api/health", "/api/login");
        jwtProperties.setSkipPaths(paths);
        
        assertNotNull(jwtProperties.getSkipPaths());
        assertEquals(3, jwtProperties.getSkipPaths().size());
        assertTrue(jwtProperties.getSkipPaths().contains("/api/public/**"));
    }

    @Test
    @DisplayName("测试自定义过期时间")
    void testCustomExpiration() {
        jwtProperties.setExpiration(3600000L);
        jwtProperties.setRefreshExpiration(86400000L);
        jwtProperties.setSecret("dGhpcy1pcy1hLXNlY3VyZS1qd3Qtc2VjcmV0LWtleS10aGF0LWlzLWxvbmc=");
        
        assertEquals(3600000L, jwtProperties.getExpiration());
        assertEquals(86400000L, jwtProperties.getRefreshExpiration());
        
        assertDoesNotThrow(() -> jwtProperties.validate());
    }

    @Test
    @DisplayName("测试密钥格式验证 - 32字符Base64")
    void testMinimumLengthSecret() {
        String minimumSecret = "dGhpcy1pcy1hLW1pbmltdW0tbGVuZ3RoLWtleS0zMg==";
        jwtProperties.setSecret(minimumSecret);
        
        assertDoesNotThrow(() -> jwtProperties.validate());
    }
}
