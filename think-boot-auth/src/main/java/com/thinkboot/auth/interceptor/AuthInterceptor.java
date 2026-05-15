package com.thinkboot.auth.interceptor;

import com.thinkboot.auth.annotation.IgnoreAuth;
import com.thinkboot.auth.config.JwtProperties;
import com.thinkboot.auth.context.UserContext;
import com.thinkboot.auth.model.LoginUser;
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
 * 认证策略：
 * 1. 外部请求（客户端 → 网关 → 微服务）：网关验证 JWT，微服务信任网关传递的 X-User-Id
 * 2. 内部调用（微服务 → Feign → 微服务）：跳过 Token 验证，信任内部调用
 * 3. 直接访问微服务：必须验证 Token（单体部署模式）
 * 
 * 放行条件（任一即可）：
 * - 路径匹配 skip-paths 配置
 * - 方法或类上标记了 @IgnoreAuth 注解
 * - 包含内部调用标记 X-Internal-Call（来自 Feign 调用）
 * - 包含 X-User-Id 头（来自网关）
 * 
 * 安全边界：网关是唯一的外部入口，内部服务走 Nacos 注册中心的信任网络
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * 网关传递的用户 ID 头
     */
    private static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 内部调用标记头（Feign 自动传递）
     */
    private static final String INTERNAL_CALL_HEADER = "X-Internal-Call";

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

        // 检查3：内部调用（Feign → 微服务），跳过 Token 验证
        String internalCall = request.getHeader(INTERNAL_CALL_HEADER);
        if ("true".equalsIgnoreCase(internalCall)) {
            String userId = request.getHeader(USER_ID_HEADER);
            if (userId != null && !userId.isEmpty()) {
                // 内部调用仍传递用户信息，但不验证 Token
                LoginUser loginUser = new LoginUser();
                loginUser.setUserId(userId);
                loginUser.setToken(null); // 内部调用无 Token
                loginUser.setExpireTime(0L);
                UserContext.setCurrentUser(loginUser);
                return true;
            }
        }

        // 检查4：网关已验证 Token，直接使用 X-User-Id
        String gatewayUserId = request.getHeader(USER_ID_HEADER);
        if (gatewayUserId != null && !gatewayUserId.isEmpty()) {
            LoginUser loginUser = new LoginUser();
            loginUser.setUserId(gatewayUserId);
            loginUser.setToken(extractToken(request));
            loginUser.setExpireTime(jwtUtils.getExpiration() / 1000);
            UserContext.setCurrentUser(loginUser);
            return true;
        }

        // 检查5：直接访问微服务（单体部署），必须验证 Token
        String token = extractToken(request);

        if (token == null || token.isEmpty()) {
            sendUnauthorizedResponse(response, "Missing authentication token");
            return false;
        }

        if (!jwtUtils.validateToken(token)) {
            sendUnauthorizedResponse(response, "Invalid or expired token");
            return false;
        }

        // Token 验证通过，设置完整的用户上下文
        LoginUser loginUser = jwtUtils.parseLoginUser(token);
        UserContext.setCurrentUser(loginUser);

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
        String tokenHeader = jwtProperties.getTokenHeader();
        String tokenPrefix = jwtProperties.getTokenPrefix();
        
        String authHeader = request.getHeader(tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenPrefix)) {
            return authHeader.substring(tokenPrefix.length());
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
