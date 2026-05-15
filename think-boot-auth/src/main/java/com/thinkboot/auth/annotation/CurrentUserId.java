package com.thinkboot.auth.annotation;

import java.lang.annotation.*;

/**
 * 当前用户 ID 注入注解
 * 
 * 用于 Controller 方法参数，自动注入当前登录用户的 ID。
 * 
 * 使用示例：
 * @GetMapping("/me")
 * public R<String> getCurrentUser(@CurrentUserId String userId) {
 *     return R.success("当前用户ID: " + userId);
 * }
 * 
 * 等同于：
 * String userId = UserContext.getCurrentUserId();
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUserId {
}
