package com.thinkboot.core.filter;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class XssFilterTest {

    @Test
    void testXssSanitization() {
        String maliciousInput = "<script>alert('XSS')</script>";
        String sanitized = HtmlUtils.htmlEscape(maliciousInput);
        
        assertEquals("&lt;script&gt;alert(&#39;XSS&#39;)&lt;/script&gt;", sanitized);
        assertFalse(sanitized.contains("<"), "Sanitized input should not contain < character");
        assertFalse(sanitized.contains(">"), "Sanitized input should not contain > character");
    }

    @Test
    void testXssSanitizationWithNullInput() {
        String input = null;
        String result = input;
        assertNull(result);
    }

    @Test
    void testXssSanitizationWithEmptyInput() {
        String input = "";
        String result = input;
        assertEquals("", result);
    }

    @Test
    void testXssSanitizationWithSafeInput() {
        String safeInput = "Hello, World! 123";
        String sanitized = HtmlUtils.htmlEscape(safeInput);
        assertEquals(safeInput, sanitized);
    }

    @Test
    void testXssSanitizationWithMixedContent() {
        String mixedInput = "<b>Bold</b> text with <script>alert('evil')</script>";
        String sanitized = HtmlUtils.htmlEscape(mixedInput);
        
        assertTrue(sanitized.contains("&lt;b&gt;"));
        assertTrue(sanitized.contains("&lt;/b&gt;"));
        assertTrue(sanitized.contains("&lt;script&gt;"));
        assertTrue(sanitized.contains("&lt;/script&gt;"));
        assertFalse(sanitized.contains("<b>"));
        assertFalse(sanitized.contains("<script>"));
    }

    @Test
    void testXssSanitizationWithSpecialCharacters() {
        String input = "\" onclick=\"alert('XSS')\" ";
        String sanitized = HtmlUtils.htmlEscape(input);
        
        assertEquals("&quot; onclick=&quot;alert(&#39;XSS&#39;)&quot; ", sanitized);
    }
}
