package com.thinkboot.core.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TraceId过滤器测试")
class TraceIdFilterTest {

    @Test
    @DisplayName("测试生成TraceId格式")
    void testTraceIdFormat() {
        String traceId = generateTraceId();
        
        assertNotNull(traceId);
        assertFalse(traceId.isEmpty());
        assertEquals(32, traceId.length());
        assertTrue(traceId.matches("[a-f0-9]+"));
    }

    @Test
    @DisplayName("测试TraceId唯一性")
    void testTraceIdUniqueness() {
        String traceId1 = generateTraceId();
        String traceId2 = generateTraceId();
        
        assertNotEquals(traceId1, traceId2);
    }

    private String generateTraceId() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
