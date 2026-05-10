package com.thinkboot.common.idempotent;

import com.thinkboot.common.result.R;
import cn.hutool.core.util.IdUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/idempotent")
@ConditionalOnProperty(prefix = "thinkboot.core", name = "idempotent-enabled", havingValue = "true", matchIfMissing = true)
public class IdempotentController {

    private static final String IDEMPOTENT_KEY_PREFIX = "idempotent:";

    private final StringRedisTemplate stringRedisTemplate;

    public IdempotentController(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("/token")
    public R<String> generateToken() {
        String token = IdUtil.fastSimpleUUID();
        String key = IDEMPOTENT_KEY_PREFIX + token;
        stringRedisTemplate.opsForValue().set(key, "1", 5, TimeUnit.SECONDS);
        return R.success(token);
    }
}
