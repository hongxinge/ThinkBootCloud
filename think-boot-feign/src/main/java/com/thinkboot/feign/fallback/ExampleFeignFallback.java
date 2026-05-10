package com.thinkboot.feign.fallback;

import com.thinkboot.common.result.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExampleFeignFallback implements ExampleFeignClient {
    private static final Logger log = LoggerFactory.getLogger(ExampleFeignFallback.class);

    @Override
    public R<String> getUserInfo(String userId) {
        log.error("getUserInfo fallback triggered, userId={}", userId);
        return R.error(503, "用户服务暂时不可用");
    }
}
