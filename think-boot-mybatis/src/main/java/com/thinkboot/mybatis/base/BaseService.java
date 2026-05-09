package com.thinkboot.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BaseService<T> extends IService<T> {

    default Page<T> page(int current, int size) {
        return page(new Page<>(current, size));
    }

    default Page<T> page(int current, int size, QueryWrapper<T> queryWrapper) {
        return page(new Page<>(current, size), queryWrapper);
    }
}
