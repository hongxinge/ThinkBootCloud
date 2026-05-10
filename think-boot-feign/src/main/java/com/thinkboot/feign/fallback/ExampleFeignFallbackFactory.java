package com.thinkboot.feign.fallback;

import com.thinkboot.common.result.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ExampleFeignFallbackFactory implements FallbackFactory<ExampleFeignClient> {
    private static final Logger log = LoggerFactory.getLogger(ExampleFeignFallbackFactory.class);

    @Override
    public ExampleFeignClient create(Throwable cause) {
        return new ExampleFeignClient() {
            @Override
            public R<String> getUserInfo(String userId) {
                log.error("getUserInfo fallback triggered, userId={}, error={}", userId, cause.getMessage());
                return R.error(503, "用户服务暂时不可用");
            }
        };
    }
}
