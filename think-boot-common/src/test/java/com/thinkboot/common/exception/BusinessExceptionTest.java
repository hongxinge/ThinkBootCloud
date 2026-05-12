package com.thinkboot.common.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("业务异常测试")
class BusinessExceptionTest {

    @Test
    @DisplayName("测试默认错误码构造")
    void testConstructorWithMessage() {
        BusinessException exception = new BusinessException("Business error");
        
        assertEquals(500, exception.getCode());
        assertEquals("Business error", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试带错误码构造")
    void testConstructorWithCodeAndMessage() {
        BusinessException exception = new BusinessException(400, "Bad request");
        
        assertEquals(400, exception.getCode());
        assertEquals("Bad request", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试带错误原因构造")
    void testConstructorWithCause() {
        Throwable cause = new NullPointerException("Null pointer");
        BusinessException exception = new BusinessException(500, "Business error", cause);
        
        assertEquals(500, exception.getCode());
        assertEquals("Business error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("测试异常继承关系")
    void testExceptionInheritance() {
        BusinessException exception = new BusinessException("Test");
        
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("测试常见业务场景错误码")
    void testCommonBusinessErrorCodes() {
        BusinessException notFound = new BusinessException(404, "Resource not found");
        BusinessException unauthorized = new BusinessException(401, "Unauthorized");
        BusinessException forbidden = new BusinessException(403, "Access denied");
        BusinessException badRequest = new BusinessException(400, "Invalid input");
        
        assertEquals(404, notFound.getCode());
        assertEquals(401, unauthorized.getCode());
        assertEquals(403, forbidden.getCode());
        assertEquals(400, badRequest.getCode());
    }

    @Test
    @DisplayName("测试异常堆栈跟踪")
    void testStackTrace() {
        BusinessException exception = new BusinessException(500, "Test error");
        StackTraceElement[] stackTrace = exception.getStackTrace();
        
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }
}
