package com.thinkboot.common.exception;

import com.thinkboot.common.result.R;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("全局异常处理器测试")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("测试业务异常")
    void testBusinessException() {
        R<?> result = handler.handleBusinessException(new BusinessException(400, "Bad request"));
        
        assertEquals(400, result.getCode());
        assertEquals("Bad request", result.getMessage());
    }

    @Test
    @DisplayName("测试业务异常 - 默认错误码")
    void testBusinessExceptionDefaultCode() {
        R<?> result = handler.handleBusinessException(new BusinessException("Server error"));
        
        assertEquals(500, result.getCode());
        assertEquals("Server error", result.getMessage());
    }

    @Test
    @DisplayName("测试参数校验异常")
    void testMethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        FieldError fieldError1 = mock(FieldError.class);
        FieldError fieldError2 = mock(FieldError.class);
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        when(fieldError1.getDefaultMessage()).thenReturn("Username is required");
        when(fieldError2.getDefaultMessage()).thenReturn("Password must be at least 6 characters");
        
        R<?> result = handler.handleMethodArgumentNotValidException(ex);
        
        assertEquals(400, result.getCode());
        assertTrue(result.getMessage().contains("Username is required"));
        assertTrue(result.getMessage().contains("Password must be at least 6 characters"));
    }

    @Test
    @DisplayName("测试绑定异常")
    void testBindException() {
        BindException ex = mock(BindException.class);
        FieldError fieldError = mock(FieldError.class);
        
        when(ex.getFieldErrors()).thenReturn(List.of(fieldError));
        when(fieldError.getDefaultMessage()).thenReturn("Invalid email format");
        
        R<?> result = handler.handleBindException(ex);
        
        assertEquals(400, result.getCode());
        assertEquals("Invalid email format", result.getMessage());
    }

    @Test
    @DisplayName("测试约束违反异常")
    void testConstraintViolationException() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        
        when(ex.getConstraintViolations()).thenReturn(Set.of(violation));
        when(violation.getMessage()).thenReturn("must not be null");
        
        R<?> result = handler.handleConstraintViolationException(ex);
        
        assertEquals(400, result.getCode());
        assertEquals("must not be null", result.getMessage());
    }

    @Test
    @DisplayName("测试缺少请求参数异常")
    void testMissingServletRequestParameterException() {
        MissingServletRequestParameterException ex = mock(MissingServletRequestParameterException.class);
        when(ex.getParameterName()).thenReturn("userId");
        
        R<?> result = handler.handleMissingServletRequestParameterException(ex);
        
        assertEquals(400, result.getCode());
        assertEquals("Missing parameter: userId", result.getMessage());
    }

    @Test
    @DisplayName("测试HTTP方法不支持异常")
    void testHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException ex = mock(HttpRequestMethodNotSupportedException.class);
        when(ex.getMethod()).thenReturn("DELETE");
        
        R<?> result = handler.handleHttpRequestMethodNotSupportedException(ex);
        
        assertEquals(405, result.getCode());
        assertTrue(result.getMessage().contains("DELETE"));
    }

    @Test
    @DisplayName("测试资源未找到异常")
    void testNoResourceFoundException() {
        NoResourceFoundException ex = mock(NoResourceFoundException.class);
        when(ex.getResourcePath()).thenReturn("/api/users/123");
        
        R<?> result = handler.handleNoResourceFoundException(ex);
        
        assertEquals(404, result.getCode());
        assertEquals("Resource not found", result.getMessage());
    }

    @Test
    @DisplayName("测试未知异常")
    void testUnknownException() {
        Exception ex = new RuntimeException("Unexpected error");
        
        R<?> result = handler.handleException(ex);
        
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("Internal server error"));
    }
}
