package com.thinkboot.auth.interceptor;

import com.thinkboot.auth.annotation.RequireLogin;
import com.thinkboot.auth.config.JwtProperties;
import com.thinkboot.auth.context.UserContext;
import com.thinkboot.auth.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;

    public AuthInterceptor(JwtUtils jwtUtils, JwtProperties jwtProperties) {
        this.jwtUtils = jwtUtils;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        if (isSkipPath(request.getRequestURI())) {
            return true;
        }

        RequireLogin methodAnnotation = handlerMethod.getMethodAnnotation(RequireLogin.class);
        RequireLogin classAnnotation = handlerMethod.getBeanType().getAnnotation(RequireLogin.class);

        if (methodAnnotation != null && !methodAnnotation.value()) {
            return true;
        }

        if (classAnnotation != null && !classAnnotation.value() && methodAnnotation == null) {
            return true;
        }

        String token = extractToken(request);

        if (token == null || token.isEmpty()) {
            sendUnauthorizedResponse(response, "Missing authentication token");
            return false;
        }

        if (!jwtUtils.validateToken(token)) {
            sendUnauthorizedResponse(response, "Invalid or expired token");
            return false;
        }

        String userId = jwtUtils.getUserId(token);
        UserContext.setCurrentUserId(userId);
        request.setAttribute("userId", userId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }

    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        return request.getParameter("token");
    }

    private boolean isSkipPath(String requestUri) {
        List<String> skipPaths = jwtProperties.getSkipPaths();
        if (skipPaths == null || skipPaths.isEmpty()) {
            return false;
        }

        for (String pattern : skipPaths) {
            if (matchPattern(pattern, requestUri)) {
                return true;
            }
        }

        return false;
    }

    private boolean matchPattern(String pattern, String requestUri) {
        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);
            return requestUri.startsWith(prefix);
        } else if (pattern.endsWith("*")) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            return requestUri.startsWith(prefix);
        }
        return requestUri.equals(pattern);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"" + message + "\"}");
    }
}
