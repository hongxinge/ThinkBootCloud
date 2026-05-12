package com.thinkboot.core.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorePropertiesTest {

    private CoreProperties coreProperties;

    @BeforeEach
    void setUp() {
        coreProperties = new CoreProperties();
    }

    @Test
    void testDefaultValues() {
        assertTrue(coreProperties.isEnableCors(), "CORS should be enabled by default");
        assertTrue(coreProperties.isEnableTraceId(), "TraceId should be enabled by default");
        assertTrue(coreProperties.isIdempotentEnabled(), "Idempotent should be enabled by default");
        assertFalse(coreProperties.isXssFilterEnabled(), "XSS filter should be disabled by default");
        assertEquals(10, coreProperties.getMaxUploadSize(), "Max upload size should be 10MB by default");
        assertNotNull(coreProperties.getCorsAllowedOrigins(), "CORS allowed origins should not be null");
        assertTrue(coreProperties.getCorsAllowedOrigins().isEmpty(), "CORS allowed origins should be empty by default");
    }

    @Test
    void testSetEnableCors() {
        coreProperties.setEnableCors(false);
        assertFalse(coreProperties.isEnableCors());
    }

    @Test
    void testSetEnableTraceId() {
        coreProperties.setEnableTraceId(false);
        assertFalse(coreProperties.isEnableTraceId());
    }

    @Test
    void testSetIdempotentEnabled() {
        coreProperties.setIdempotentEnabled(false);
        assertFalse(coreProperties.isIdempotentEnabled());
    }

    @Test
    void testSetXssFilterEnabled() {
        coreProperties.setXssFilterEnabled(true);
        assertTrue(coreProperties.isXssFilterEnabled());
    }

    @Test
    void testSetMaxUploadSize() {
        coreProperties.setMaxUploadSize(50);
        assertEquals(50, coreProperties.getMaxUploadSize());
    }

    @Test
    void testSetCorsAllowedOrigins() {
        coreProperties.setCorsAllowedOrigins(java.util.List.of("http://localhost:3000", "http://example.com"));
        assertEquals(2, coreProperties.getCorsAllowedOrigins().size());
        assertTrue(coreProperties.getCorsAllowedOrigins().contains("http://localhost:3000"));
        assertTrue(coreProperties.getCorsAllowedOrigins().contains("http://example.com"));
    }
}
