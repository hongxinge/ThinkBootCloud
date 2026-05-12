package com.thinkboot.gateway.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("网关配置属性测试")
class GatewayPropertiesTest {

    private GatewayProperties gatewayProperties;

    @BeforeEach
    void setUp() {
        gatewayProperties = new GatewayProperties();
    }

    @Test
    @DisplayName("测试默认值")
    void testDefaultValues() {
        assertFalse(gatewayProperties.isRateLimitEnable());
        assertEquals(100, gatewayProperties.getRateLimitCount());
        assertEquals(60, gatewayProperties.getRateLimitWindow());
        assertNull(gatewayProperties.getJwtSecret());
        assertNotNull(gatewayProperties.getSkipAuthPaths());
        assertTrue(gatewayProperties.getSkipAuthPaths().isEmpty());
        assertNotNull(gatewayProperties.getCorsAllowedOrigins());
        assertTrue(gatewayProperties.getCorsAllowedOrigins().isEmpty());
    }

    @Test
    @DisplayName("测试设置限流配置")
    void testSetRateLimitConfig() {
        gatewayProperties.setRateLimitEnable(true);
        gatewayProperties.setRateLimitCount(200);
        gatewayProperties.setRateLimitWindow(120);
        gatewayProperties.setJwtSecret("dGhpcy1pcy1hLXNlY3VyZS1qd3Qtc2VjcmV0LWtleS10aGF0LWlzLWxvbmc=");
        
        assertTrue(gatewayProperties.isRateLimitEnable());
        assertEquals(200, gatewayProperties.getRateLimitCount());
        assertEquals(120, gatewayProperties.getRateLimitWindow());
        
        assertDoesNotThrow(() -> gatewayProperties.validate());
    }

    @Test
    @DisplayName("测试设置跳过认证路径")
    void testSetSkipAuthPaths() {
        gatewayProperties.setSkipAuthPaths(List.of("/api/public/**", "/api/health"));
        
        assertEquals(2, gatewayProperties.getSkipAuthPaths().size());
        assertTrue(gatewayProperties.getSkipAuthPaths().contains("/api/public/**"));
    }

    @Test
    @DisplayName("测试设置CORS允许来源")
    void testSetCorsAllowedOrigins() {
        gatewayProperties.setCorsAllowedOrigins(List.of("http://localhost:3000", "https://example.com"));
        
        assertEquals(2, gatewayProperties.getCorsAllowedOrigins().size());
        assertTrue(gatewayProperties.getCorsAllowedOrigins().contains("http://localhost:3000"));
    }

    @Test
    @DisplayName("测试空JWT密钥验证失败")
    void testEmptyJwtSecretValidation() {
        gatewayProperties.setJwtSecret("");
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> gatewayProperties.validate());
        assertTrue(exception.getMessage().contains("Gateway JWT Secret must not be empty"));
    }

    @Test
    @DisplayName("测试null JWT密钥验证失败")
    void testNullJwtSecretValidation() {
        gatewayProperties.setJwtSecret(null);
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> gatewayProperties.validate());
        assertTrue(exception.getMessage().contains("Gateway JWT Secret must not be empty"));
    }

    @Test
    @DisplayName("测试过短JWT密钥验证失败")
    void testShortJwtSecretValidation() {
        gatewayProperties.setJwtSecret("dGhpcy1pcy1zaG9ydA==");
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> gatewayProperties.validate());
        assertTrue(exception.getMessage().contains("Gateway JWT Secret must be a valid Base64-encoded key"));
    }

    @Test
    @DisplayName("测试有效JWT密钥验证成功")
    void testValidJwtSecretValidation() {
        String validSecret = "dGhpcy1pcy1hLXNlY3VyZS1qd3Qtc2VjcmV0LWtleS10aGF0LWlzLWxvbmc=";
        gatewayProperties.setJwtSecret(validSecret);
        
        assertDoesNotThrow(() -> gatewayProperties.validate());
    }
}
