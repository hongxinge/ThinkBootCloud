package com.thinkboot.redis.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    private final RedisTemplate<String, Object> template;

    public RedisUtils(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    @PostConstruct
    public void init() {
        redisTemplate = this.template;
    }

    public static void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public static Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public static Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public static Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    public static void setCacheList(String key, List<Object> value) {
        redisTemplate.opsForList().rightPushAll(key, value.toArray());
    }

    public static List<Object> getCacheList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public static void setCacheMap(String key, Map<String, Object> value) {
        if (value != null) {
            redisTemplate.opsForHash().putAll(key, value);
        }
    }

    public static Map<Object, Object> getCacheMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public static void setCacheSet(String key, Set<Object> value) {
        if (value != null) {
            redisTemplate.opsForSet().add(key, value.toArray());
        }
    }

    public static Set<Object> getCacheSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }
}
