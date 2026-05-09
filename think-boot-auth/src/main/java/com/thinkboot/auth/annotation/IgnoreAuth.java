package com.thinkboot.auth.annotation;

import java.lang.annotation.*;

/**
 * 标记忽略认证的接口
 * 
 * 使用场景：登录、注册、公开信息、分享链接等不需要 Token 验证的接口
 * 
 * 示例：
 *   @IgnoreAuth
 *   @PostMapping("/login")
 *   public R login() { ... }
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {
}
