package com.thinkboot.auth.context;

import com.thinkboot.auth.model.LoginUser;

/**
 * 用户上下文
 * 
 * 基于 ThreadLocal 存储当前登录用户信息，确保线程安全。
 * 
 * 使用方式：
 * 1. 拦截器自动设置：AuthInterceptor 在请求开始时设置，请求结束时清理
 * 2. 业务代码获取：UserContext.getCurrentUser() / UserContext.getCurrentUserId()
 * 
 * 注意：ThreadLocal 必须在请求结束时调用 clear() 清理，防止内存泄漏
 */
public class UserContext {

    private static final ThreadLocal<LoginUser> CURRENT_USER = new ThreadLocal<>();

    /**
     * 设置当前登录用户
     */
    public static void setCurrentUser(LoginUser user) {
        CURRENT_USER.set(user);
    }

    /**
     * 获取当前登录用户完整信息
     * 
     * @return LoginUser 对象，包含 userId、token、claims 等
     */
    public static LoginUser getCurrentUser() {
        return CURRENT_USER.get();
    }

    /**
     * 获取当前登录用户 ID
     * 
     * @return 用户 ID，如果未登录返回 null
     */
    public static String getCurrentUserId() {
        LoginUser user = CURRENT_USER.get();
        return user != null ? user.getUserId() : null;
    }

    /**
     * 获取当前登录用户的 Token
     * 
     * @return JWT Token，如果未登录返回 null
     */
    public static String getCurrentToken() {
        LoginUser user = CURRENT_USER.get();
        return user != null ? user.getToken() : null;
    }

    /**
     * 判断当前用户是否已登录
     * 
     * @return true 如果已登录
     */
    public static boolean isLoggedIn() {
        return CURRENT_USER.get() != null;
    }

    /**
     * 清理 ThreadLocal，防止内存泄漏
     * 
     * 必须在请求结束时调用（通常在 Interceptor.afterCompletion 中）
     */
    public static void clear() {
        CURRENT_USER.remove();
    }
}
