package com.thinkboot.common.result;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("分页请求测试")
class PageRequestTest {

    @Test
    @DisplayName("测试默认值")
    void testDefaultValues() {
        PageRequest request = new PageRequest();
        
        assertEquals(1, request.getPageNo());
        assertEquals(10, request.getPageSize());
        assertNull(request.getOrderBy());
        assertEquals("ASC", request.getOrderDirection());
    }

    @Test
    @DisplayName("测试设置分页参数")
    void testSetPageParameters() {
        PageRequest request = new PageRequest();
        request.setPageNo(5);
        request.setPageSize(20);
        request.setOrderBy("create_time");
        request.setOrderDirection("DESC");
        
        assertEquals(5, request.getPageNo());
        assertEquals(20, request.getPageSize());
        assertEquals("create_time", request.getOrderBy());
        assertEquals("DESC", request.getOrderDirection());
    }

    @Test
    @DisplayName("测试页码小于1时修正为1")
    void testPageNoLessThanOne() {
        PageRequest request = new PageRequest();
        request.setPageNo(0);
        
        assertEquals(1, request.getPageNo());
    }

    @Test
    @DisplayName("测试页码为负数时修正为1")
    void testNegativePageNo() {
        PageRequest request = new PageRequest();
        request.setPageNo(-5);
        
        assertEquals(1, request.getPageNo());
    }

    @Test
    @DisplayName("测试页大小小于1时修正为10")
    void testPageSizeLessThanOne() {
        PageRequest request = new PageRequest();
        request.setPageSize(0);
        
        assertEquals(10, request.getPageSize());
    }

    @Test
    @DisplayName("测试页大小大于100时修正为100")
    void testPageSizeGreaterThan100() {
        PageRequest request = new PageRequest();
        request.setPageSize(200);
        
        assertEquals(100, request.getPageSize());
    }

    @Test
    @DisplayName("测试正常范围内的页大小")
    void testNormalPageSize() {
        PageRequest request = new PageRequest();
        request.setPageSize(50);
        
        assertEquals(50, request.getPageSize());
    }

    @Test
    @DisplayName("测试页大小边界值 - 1")
    void testPageSizeBoundaryMin() {
        PageRequest request = new PageRequest();
        request.setPageSize(1);
        
        assertEquals(1, request.getPageSize());
    }

    @Test
    @DisplayName("测试页大小边界值 - 100")
    void testPageSizeBoundaryMax() {
        PageRequest request = new PageRequest();
        request.setPageSize(100);
        
        assertEquals(100, request.getPageSize());
    }

    @Test
    @DisplayName("测试排序字段为空")
    void testNullOrderBy() {
        PageRequest request = new PageRequest();
        request.setOrderBy(null);
        
        assertNull(request.getOrderBy());
    }

    @Test
    @DisplayName("测试默认排序方向")
    void testDefaultOrderDirection() {
        PageRequest request = new PageRequest();
        
        assertEquals("ASC", request.getOrderDirection());
    }
}
