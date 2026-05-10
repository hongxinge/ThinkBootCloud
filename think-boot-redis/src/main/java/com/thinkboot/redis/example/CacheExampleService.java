package com.thinkboot.redis.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 响应缓存使用示例
 * 
 * @Cacheable: 先查缓存，没有则执行方法并缓存结果
 * @CachePut: 执行方法并更新缓存
 * @CacheEvict: 执行方法并清除缓存
 * 
 * 开发者直接参考此类实现自己的缓存逻辑
 */
@Service
public class CacheExampleService {
    private static final Logger log = LoggerFactory.getLogger(CacheExampleService.class);

    private static final String CACHE_NAME = "example:data";

    private final Map<String, String> mockDatabase = new HashMap<>();
    private final AtomicLong callCount = new AtomicLong(0);

    public CacheExampleService() {
        mockDatabase.put("user:1", "Alice");
        mockDatabase.put("user:2", "Bob");
    }

    @Cacheable(value = CACHE_NAME, key = "#id", unless = "#result == null")
    public String getById(String id) {
        callCount.incrementAndGet();
        log.info("Cache miss, querying database for id={}", id);
        return mockDatabase.get(id);
    }

    @CachePut(value = CACHE_NAME, key = "#id")
    public String update(String id, String value) {
        log.info("Updating cache for id={}", id);
        mockDatabase.put(id, value);
        return value;
    }

    @CacheEvict(value = CACHE_NAME, key = "#id")
    public void delete(String id) {
        log.info("Evicting cache for id={}", id);
        mockDatabase.remove(id);
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void clearAllCache() {
        log.info("Clearing all cache entries");
        mockDatabase.clear();
    }

    public long getCallCount() {
        return callCount.get();
    }
}
