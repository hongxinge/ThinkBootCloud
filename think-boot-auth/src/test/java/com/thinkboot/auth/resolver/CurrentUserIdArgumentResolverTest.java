package com.thinkboot.auth.resolver;

import com.thinkboot.auth.annotation.CurrentUserId;
import com.thinkboot.auth.context.UserContext;
import com.thinkboot.auth.model.LoginUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CurrentUserId参数解析器测试")
class CurrentUserIdArgumentResolverTest {

    private CurrentUserIdArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new CurrentUserIdArgumentResolver();
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    @DisplayName("测试支持带@CurrentUserId注解的String参数")
    void testSupportsParameterWithAnnotation() throws Exception {
        Method method = TestController.class.getMethod("testMethod", String.class);
        MethodParameter param = new MethodParameter(method, 0);
        
        assertTrue(resolver.supportsParameter(param));
    }

    @Test
    @DisplayName("测试不支持无注解的参数")
    void testDoesNotSupportParameterWithoutAnnotation() throws Exception {
        Method method = TestController.class.getMethod("noAnnotationMethod", String.class);
        MethodParameter param = new MethodParameter(method, 0);
        
        assertFalse(resolver.supportsParameter(param));
    }

    @Test
    @DisplayName("测试不支持非String类型参数")
    void testDoesNotSupportNonStringType() throws Exception {
        Method method = TestController.class.getMethod("longParamMethod", Long.class);
        MethodParameter param = new MethodParameter(method, 0);
        
        assertFalse(resolver.supportsParameter(param));
    }

    @Test
    @DisplayName("测试解析已登录用户ID")
    void testResolveArgumentWithLoggedInUser() {
        LoginUser user = new LoginUser();
        user.setUserId("user123");
        UserContext.setCurrentUser(user);
        
        Object result = resolver.resolveArgument(null, null, null, null);
        
        assertEquals("user123", result);
    }

    @Test
    @DisplayName("测试解析未登录用户返回null")
    void testResolveArgumentWithNoUser() {
        Object result = resolver.resolveArgument(null, null, null, null);
        
        assertNull(result);
    }

    static class TestController {
        public void testMethod(@CurrentUserId String userId) {}
        public void noAnnotationMethod(String userId) {}
        public void longParamMethod(@CurrentUserId Long userId) {}
    }
}
