package com.thinkboot.common.idempotent;

import java.lang.annotation.*;

/**
 * 幂等性注解
 * 标记接口自动进行防重复提交校验
 * 基于 Redis + Token 机制实现
 * 
 * 使用方式：
 * 1. 先调用 GET /api/idempotent/token 获取 Token
 * 2. 在请求头中携带 X-Idempotent-Token
 * 3. 被 @Idempotent 标记的接口自动校验
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /**
     * 业务键前缀
     */
    String key() default "";

    /**
     * Token 过期时间（秒）
     */
    long expire() default 5;

    /**
     * 错误提示信息
     */
    String message() default "请勿重复提交";
}
