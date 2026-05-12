package com.thinkboot.common.result;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("统一响应结果测试")
class RTest {

    @Test
    @DisplayName("测试成功响应无数据")
    void testSuccessWithoutData() {
        R<Void> result = R.success();
        
        assertEquals(R.SUCCESS, result.getCode());
        assertEquals("success", result.getMessage());
        assertNull(result.getData());
        assertTrue(result.getTimestamp() > 0);
    }

    @Test
    @DisplayName("测试成功响应带数据")
    void testSuccessWithData() {
        String data = "test data";
        R<String> result = R.success(data);
        
        assertEquals(R.SUCCESS, result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals(data, result.getData());
        assertTrue(result.getTimestamp() > 0);
    }

    @Test
    @DisplayName("测试成功响应带复杂对象")
    void testSuccessWithComplexObject() {
        TestUser user = new TestUser(1L, "testuser", "test@example.com");
        R<TestUser> result = R.success(user);
        
        assertEquals(R.SUCCESS, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1L, result.getData().id());
        assertEquals("testuser", result.getData().username());
    }

    @Test
    @DisplayName("测试错误响应带自定义错误码")
    void testErrorWithCode() {
        R<Void> result = R.error(400, "Bad request");
        
        assertEquals(400, result.getCode());
        assertEquals("Bad request", result.getMessage());
        assertNull(result.getData());
        assertTrue(result.getTimestamp() > 0);
    }

    @Test
    @DisplayName("测试错误响应默认错误码")
    void testErrorDefaultCode() {
        R<Void> result = R.error("Internal server error");
        
        assertEquals(R.ERROR, result.getCode());
        assertEquals("Internal server error", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("测试静态常量定义")
    void testStatusCodes() {
        assertEquals(200, R.SUCCESS);
        assertEquals(500, R.ERROR);
        assertEquals(401, R.UNAUTHORIZED);
        assertEquals(403, R.FORBIDDEN);
        assertEquals(404, R.NOT_FOUND);
    }

    @Test
    @DisplayName("测试成功响应带列表数据")
    void testSuccessWithList() {
        List<String> data = List.of("item1", "item2", "item3");
        R<List<String>> result = R.success(data);
        
        assertEquals(R.SUCCESS, result.getCode());
        assertNotNull(result.getData());
        assertEquals(3, result.getData().size());
    }

    @Test
    @DisplayName("测试构建器模式 - 手动设置所有字段")
    void testManualConstruction() {
        R<String> result = new R<>(200, "Custom message", "data", 1234567890L);
        
        assertEquals(200, result.getCode());
        assertEquals("Custom message", result.getMessage());
        assertEquals("data", result.getData());
        assertEquals(1234567890L, result.getTimestamp());
    }

    record TestUser(Long id, String username, String email) {}
}
