package com.thinkboot.common.idempotent;

import com.thinkboot.common.exception.BusinessException;
import com.thinkboot.common.result.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

public class IdempotentInterceptor implements HandlerInterceptor {

    private static final String IDEMPOTENT_TOKEN_HEADER = "X-Idempotent-Token";
    private static final String IDEMPOTENT_KEY_PREFIX = "idempotent:";

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public IdempotentInterceptor(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Idempotent idempotent = handlerMethod.getMethodAnnotation(Idempotent.class);
        if (idempotent == null) {
            return true;
        }

        String token = request.getHeader(IDEMPOTENT_TOKEN_HEADER);
        if (token == null || token.isBlank()) {
            writeResponse(response, R.error(400, "缺少幂等性 Token"));
            return false;
        }

        String key = buildKey(idempotent, token);
        Boolean deleted = stringRedisTemplate.delete(key);
        if (Boolean.FALSE.equals(deleted)) {
            writeResponse(response, R.error(400, idempotent.message()));
            return false;
        }

        return true;
    }

    private String buildKey(Idempotent idempotent, String token) {
        String prefix = idempotent.key().isEmpty() ? IDEMPOTENT_KEY_PREFIX : IDEMPOTENT_KEY_PREFIX + idempotent.key() + ":";
        return prefix + token;
    }

    private void writeResponse(HttpServletResponse response, R<?> r) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(r));
        } catch (Exception e) {
            throw new BusinessException("响应写入失败");
        }
    }
}
