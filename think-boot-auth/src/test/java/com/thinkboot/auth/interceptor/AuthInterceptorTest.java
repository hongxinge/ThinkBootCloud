package com.thinkboot.auth.interceptor;

import com.thinkboot.auth.annotation.IgnoreAuth;
import com.thinkboot.auth.config.JwtProperties;
import com.thinkboot.auth.context.UserContext;
import com.thinkboot.auth.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.web.method.HandlerMethod;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("认证拦截器测试")
class AuthInterceptorTest {

    private AuthInterceptor interceptor;
    private JwtUtils jwtUtils;
    private JwtProperties jwtProperties;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret("dGhpcy1pcy1hLXNlY3VyZS1qd3Qtc2VjcmV0LWtleS10aGF0LWlzLWxvbmc=");
        jwtProperties.setSkipPaths(List.of("/api/public/**", "/api/health"));
        
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        
        try {
            when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
        } catch (Exception e) {
            fail(e);
        }
        
        interceptor = new AuthInterceptor(jwtUtils, jwtProperties);
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    @DisplayName("测试1：非控制器请求放行")
    void testNonControllerRequest() throws Exception {
        Object handler = new Object();
        
        boolean result = interceptor.preHandle(request, response, handler);
        
        assertTrue(result);
        verify(request, never()).getHeader(anyString());
    }

    @Test
    @DisplayName("测试2：免认证路径放行")
    void testSkipPath() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/public/info");
        HandlerMethod handler = createMockHandlerMethod();
        
        boolean result = interceptor.preHandle(request, response, handler);
        
        assertTrue(result);
        verify(request, never()).getHeader("Authorization");
    }

    @Test
    @DisplayName("测试3：健康检查路径放行")
    void testHealthPath() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/health");
        HandlerMethod handler = createMockHandlerMethod();
        
        boolean result = interceptor.preHandle(request, response, handler);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("测试4：@IgnoreAuth注解方法放行")
    void testIgnoreAuthAnnotation() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/test");
        HandlerMethod handler = createMockHandlerMethodWithIgnoreAuth();
        
        boolean result = interceptor.preHandle(request, response, handler);
        
        assertTrue(result);
        verify(request, never()).getHeader("Authorization");
    }

    @Test
    @DisplayName("测试5：内部调用跳过Token验证")
    void testInternalCall() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/order/create");
        when(request.getHeader("X-Internal-Call")).thenReturn("true");
        when(request.getHeader("X-User-Id")).thenReturn("user123");
        
        HandlerMethod handler = createMockHandlerMethod();
        boolean result = interceptor.preHandle(request, response, handler);
        
        assertTrue(result);
        assertEquals("user123", UserContext.getCurrentUserId());
        assertNull(UserContext.getCurrentUser().getToken());
        verify(jwtUtils, never()).validateToken(anyString());
    }

    @Test
    @DisplayName("测试6：网关模式 - 信任X-User-Id")
    void testGatewayMode() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/user/info");
        when(request.getHeader("X-User-Id")).thenReturn("user456");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtUtils.getExpiration()).thenReturn(7200000L);
        
        HandlerMethod handler = createMockHandlerMethod();
        boolean result = interceptor.preHandle(request, response, handler);
        
        assertTrue(result);
        assertEquals("user456", UserContext.getCurrentUserId());
        assertEquals("valid-token", UserContext.getCurrentUser().getToken());
        verify(jwtUtils, never()).validateToken(anyString());
    }

    @Test
    @DisplayName("测试7：单体模式 - Token验证成功")
    void testStandaloneModeValidToken() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/user/info");
        when(request.getHeader("X-User-Id")).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtUtils.validateToken("valid-token")).thenReturn(true);
        when(jwtUtils.getExpiration()).thenReturn(7200000L);
        
        com.thinkboot.auth.model.LoginUser mockUser = new com.thinkboot.auth.model.LoginUser();
        mockUser.setUserId("user789");
        when(jwtUtils.parseLoginUser("valid-token")).thenReturn(mockUser);
        
        HandlerMethod handler = createMockHandlerMethod();
        boolean result = interceptor.preHandle(request, response, handler);
        
        assertTrue(result);
        assertEquals("user789", UserContext.getCurrentUserId());
        verify(jwtUtils).validateToken("valid-token");
    }

    @Test
    @DisplayName("测试8：单体模式 - Token缺失返回401")
    void testStandaloneModeMissingToken() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/secure/data");
        when(request.getHeader("X-User-Id")).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn(null);
        
        HandlerMethod handler = createMockHandlerMethod();
        boolean result = interceptor.preHandle(request, response, handler);
        
        assertFalse(result);
        assertTrue(responseWriter.toString().contains("401"));
        assertTrue(responseWriter.toString().contains("Missing authentication token"));
    }

    @Test
    @DisplayName("测试9：单体模式 - Token无效返回401")
    void testStandaloneModeInvalidToken() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/secure/data");
        when(request.getHeader("X-User-Id")).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtUtils.validateToken("invalid-token")).thenReturn(false);
        
        HandlerMethod handler = createMockHandlerMethod();
        boolean result = interceptor.preHandle(request, response, handler);
        
        assertFalse(result);
        assertTrue(responseWriter.toString().contains("401"));
        assertTrue(responseWriter.toString().contains("Invalid or expired token"));
    }

    @Test
    @DisplayName("测试10：afterCompletion清理ThreadLocal")
    void testAfterCompletionClearsContext() throws Exception {
        UserContext.setCurrentUser(new com.thinkboot.auth.model.LoginUser());
        assertNotNull(UserContext.getCurrentUser());
        
        interceptor.afterCompletion(request, response, null, null);
        
        assertNull(UserContext.getCurrentUser());
    }

    @Test
    @DisplayName("测试11：内部调用但缺少X-User-Id继续验证")
    void testInternalCallMissingUserId() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/order/create");
        when(request.getHeader("X-Internal-Call")).thenReturn("true");
        when(request.getHeader("X-User-Id")).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn(null);
        
        HandlerMethod handler = createMockHandlerMethod();
        boolean result = interceptor.preHandle(request, response, handler);
        
        assertFalse(result);
    }

    private HandlerMethod createMockHandlerMethod() {
        try {
            Object controller = new Object();
            Method method = Object.class.getMethod("toString");
            return new HandlerMethod(controller, method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private HandlerMethod createMockHandlerMethodWithIgnoreAuth() {
        try {
            TestController controller = new TestController();
            Method method = TestController.class.getMethod("testMethod");
            return new HandlerMethod(controller, method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    static class TestController {
        @IgnoreAuth
        public void testMethod() {}
    }
}
