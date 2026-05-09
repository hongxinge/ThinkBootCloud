package com.thinkboot.auth.interceptor;

import com.thinkboot.auth.annotation.IgnoreAuth;
import com.thinkboot.auth.config.JwtProperties;
import com.thinkboot.auth.context.UserContext;
import com.thinkboot.auth.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * 认证拦截器
 * 
 * 认证策略：默认所有接口都需要 Token 验证，只有以下情况放行：
 * 1. 路径匹配 skip-paths 配置
 * 2. 方法或类上标记了 @IgnoreAuth 注解
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;
    private final AntPathMatcher pathMatcher;

    public AuthInterceptor(JwtUtils jwtUtils, JwtProperties jwtProperties) {
        this.jwtUtils = jwtUtils;
        this.jwtProperties = jwtProperties;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 非控制器请求，直接放行（如静态资源）
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 检查1：路径是否在配置的免认证列表中
        String requestUri = request.getRequestURI();
        if (isSkipPath(requestUri)) {
            return true;
        }

        // 检查2：方法或类上是否标记了 @IgnoreAuth 注解
        IgnoreAuth methodAnnotation = handlerMethod.getMethodAnnotation(IgnoreAuth.class);
        IgnoreAuth classAnnotation = handlerMethod.getBeanType().getAnnotation(IgnoreAuth.class);

        if (methodAnnotation != null || classAnnotation != null) {
            return true;
        }

        // 以上条件都不满足，必须验证 Token
        String token = extractToken(request);

        if (token == null || token.isEmpty()) {
            sendUnauthorizedResponse(response, "Missing authentication token");
            return false;
        }

        if (!jwtUtils.validateToken(token)) {
            sendUnauthorizedResponse(response, "Invalid or expired token");
            return false;
        }

        // Token 验证通过，设置用户上下文
        String userId = jwtUtils.getUserId(token);
        UserContext.setCurrentUserId(userId);
        request.setAttribute("userId", userId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理 ThreadLocal，防止内存泄漏
        UserContext.clear();
    }

    /**
     * 从请求中提取 Token
     * 优先从 Authorization Header 获取，其次从请求参数获取
     */
    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        return request.getParameter("token");
    }

    /**
     * 检查路径是否在配置的免认证列表中
     */
    private boolean isSkipPath(String requestUri) {
        List<String> skipPaths = jwtProperties.getSkipPaths();
        if (skipPaths == null || skipPaths.isEmpty()) {
            return false;
        }

        return skipPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }

    /**
     * 返回 401 未授权响应
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"" + message + "\"}");
    }
}
