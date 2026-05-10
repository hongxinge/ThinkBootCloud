package com.thinkboot.feign.fallback;

import com.thinkboot.common.result.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFallback {
    private static final Logger log = LoggerFactory.getLogger(DefaultFallback.class);

    public static <T> R<T> fallback(String serviceName, Throwable cause) {
        log.error("Feign fallback: service={}, error={}", serviceName, cause.getMessage());
        return R.error(503, "服务暂时不可用，请稍后重试");
    }

    public static <T> R<T> fallback(String serviceName) {
        return fallback(serviceName, new RuntimeException("未知错误"));
    }
}
