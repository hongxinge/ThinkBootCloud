package com.thinkboot.auth.context;

import com.thinkboot.auth.model.LoginUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("用户上下文测试")
class UserContextTest {

    @BeforeEach
    void setUp() {
        UserContext.clear();
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    @DisplayName("测试设置和获取用户")
    void testSetAndGetUser() {
        LoginUser user = new LoginUser();
        user.setUserId("user123");
        user.setToken("test-token");
        user.setExpireTime(7200L);
        
        UserContext.setCurrentUser(user);
        
        LoginUser retrieved = UserContext.getCurrentUser();
        assertNotNull(retrieved);
        assertEquals("user123", retrieved.getUserId());
        assertEquals("test-token", retrieved.getToken());
        assertEquals(7200L, retrieved.getExpireTime());
    }

    @Test
    @DisplayName("测试获取用户ID")
    void testGetCurrentUserId() {
        assertNull(UserContext.getCurrentUserId(), "未设置用户时应返回null");
        
        LoginUser user = new LoginUser();
        user.setUserId("user456");
        UserContext.setCurrentUser(user);
        
        assertEquals("user456", UserContext.getCurrentUserId());
    }

    @Test
    @DisplayName("测试获取Token")
    void testGetCurrentToken() {
        assertNull(UserContext.getCurrentToken(), "未设置用户时应返回null");
        
        LoginUser user = new LoginUser();
        user.setToken("my-jwt-token");
        UserContext.setCurrentUser(user);
        
        assertEquals("my-jwt-token", UserContext.getCurrentToken());
    }

    @Test
    @DisplayName("测试是否已登录")
    void testIsLoggedIn() {
        assertFalse(UserContext.isLoggedIn(), "未设置用户时应返回false");
        
        LoginUser user = new LoginUser();
        user.setUserId("user789");
        UserContext.setCurrentUser(user);
        
        assertTrue(UserContext.isLoggedIn());
    }

    @Test
    @DisplayName("测试清理用户上下文")
    void testClear() {
        LoginUser user = new LoginUser();
        user.setUserId("user123");
        UserContext.setCurrentUser(user);
        
        assertNotNull(UserContext.getCurrentUser());
        
        UserContext.clear();
        
        assertNull(UserContext.getCurrentUser());
        assertNull(UserContext.getCurrentUserId());
        assertFalse(UserContext.isLoggedIn());
    }

    @Test
    @DisplayName("测试LoginUser的Claims功能")
    void testLoginUserClaims() {
        LoginUser user = new LoginUser();
        user.setUserId("user123");
        user.setClaims(Map.of(
            "username", "testuser",
            "role", "USER",
            "email", "test@example.com"
        ));
        
        UserContext.setCurrentUser(user);
        
        assertEquals("testuser", user.getClaim("username"));
        assertEquals("USER", user.getClaim("role"));
        assertEquals("test@example.com", user.getClaim("email"));
        assertNull(user.getClaim("nonexistent"));
    }

    @Test
    @DisplayName("测试LoginUser的泛型Claim获取")
    void testGetClaimWithType() {
        LoginUser user = new LoginUser();
        user.setClaims(Map.of(
            "username", "admin",
            "isAdmin", true
        ));
        
        String username = user.getClaim("username", String.class);
        Boolean isAdmin = user.getClaim("isAdmin", Boolean.class);
        
        assertEquals("admin", username);
        assertTrue(isAdmin);
    }

    @Test
    @DisplayName("测试ThreadLocal隔离性")
    void testThreadLocalIsolation() {
        LoginUser user1 = new LoginUser();
        user1.setUserId("thread1-user");
        UserContext.setCurrentUser(user1);
        
        assertEquals("thread1-user", UserContext.getCurrentUserId());
        
        UserContext.clear();
        
        assertNull(UserContext.getCurrentUserId());
    }
}
