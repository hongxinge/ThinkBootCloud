package com.thinkboot.common.result;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("分页响应测试")
class PageResponseTest {

    @Test
    @DisplayName("测试创建分页响应")
    void testCreatePageResponse() {
        List<String> records = List.of("item1", "item2", "item3");
        PageResponse<String> response = PageResponse.success(records, 100, 10, 1, 10);
        
        assertEquals(R.SUCCESS, response.getCode());
        assertEquals("success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(3, response.getData().getRecords().size());
        assertEquals(100, response.getData().getTotal());
        assertEquals(10, response.getData().getPages());
        assertEquals(1, response.getData().getPageNo());
        assertEquals(10, response.getData().getPageSize());
    }

    @Test
    @DisplayName("测试分页响应 - 第一页")
    void testFirstPage() {
        List<String> records = List.of("item1");
        PageResponse<String> response = PageResponse.success(records, 50, 5, 1, 10);
        
        assertFalse(response.getData().hasPrevious());
        assertTrue(response.getData().hasNext());
    }

    @Test
    @DisplayName("测试分页响应 - 中间页")
    void testMiddlePage() {
        List<String> records = List.of("item1");
        PageResponse<String> response = PageResponse.success(records, 50, 5, 3, 10);
        
        assertTrue(response.getData().hasPrevious());
        assertTrue(response.getData().hasNext());
    }

    @Test
    @DisplayName("测试分页响应 - 最后一页")
    void testLastPage() {
        List<String> records = List.of("item1");
        PageResponse<String> response = PageResponse.success(records, 50, 5, 5, 10);
        
        assertTrue(response.getData().hasPrevious());
        assertFalse(response.getData().hasNext());
    }

    @Test
    @DisplayName("测试分页响应 - 只有一页")
    void testSinglePage() {
        List<String> records = List.of("item1", "item2");
        PageResponse<String> response = PageResponse.success(records, 2, 1, 1, 10);
        
        assertFalse(response.getData().hasPrevious());
        assertFalse(response.getData().hasNext());
    }

    @Test
    @DisplayName("测试分页响应 - 空记录")
    void testEmptyRecords() {
        List<String> records = List.of();
        PageResponse<String> response = PageResponse.success(records, 0, 0, 1, 10);
        
        assertNotNull(response.getData());
        assertTrue(response.getData().getRecords().isEmpty());
        assertEquals(0, response.getData().getTotal());
        assertEquals(0, response.getData().getPages());
        assertFalse(response.getData().hasPrevious());
        assertFalse(response.getData().hasNext());
    }

    @Test
    @DisplayName("测试 PageData 构造函数")
    void testPageDataConstructor() {
        List<Integer> records = List.of(1, 2, 3);
        PageResponse.PageData<Integer> pageData = new PageResponse.PageData<>(records, 30, 3, 2, 10);
        
        assertEquals(records, pageData.getRecords());
        assertEquals(30, pageData.getTotal());
        assertEquals(3, pageData.getPages());
        assertEquals(2, pageData.getPageNo());
        assertEquals(10, pageData.getPageSize());
    }
}
