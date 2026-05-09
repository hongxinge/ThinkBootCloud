package com.thinkboot.mybatis.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinkboot.common.result.PageResponse;

public class PageUtils {

    private PageUtils() {
    }

    public static <T> Page<T> toPage(int current, int size) {
        return new Page<>(current, size);
    }

    public static <T> Page<T> toPage(int current, int size, boolean searchCount) {
        Page<T> page = new Page<>(current, size);
        page.setSearchCount(searchCount);
        return page;
    }

    public static <T> PageResponse<T> toPageResponse(Page<T> page) {
        return PageResponse.success(
                page.getRecords(),
                page.getTotal(),
                page.getPages(),
                (int) page.getCurrent(),
                (int) page.getSize()
        );
    }
}
