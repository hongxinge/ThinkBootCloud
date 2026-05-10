package com.thinkboot.feign.fallback;

import com.thinkboot.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "think-boot-user",
    path = "/api/user",
    fallbackFactory = ExampleFeignFallbackFactory.class
)
public interface ExampleFeignClient {

    @GetMapping("/{userId}")
    R<String> getUserInfo(@PathVariable("userId") String userId);
}
