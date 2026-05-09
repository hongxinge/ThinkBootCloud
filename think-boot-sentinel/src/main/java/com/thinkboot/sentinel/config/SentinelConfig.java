package com.thinkboot.sentinel.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SentinelConfig {

    @Bean
    public BlockExceptionHandler thinkBootBlockExceptionHandler() {
        return (HttpServletRequest request, HttpServletResponse response, BlockException e) -> {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> result = new HashMap<>();
            result.put("code", 429);
            
            if (e instanceof FlowException) {
                result.put("message", "请求过于频繁，请稍后再试");
            } else if (e instanceof DegradeException) {
                result.put("message", "服务降级，请稍后再试");
            } else {
                result.put("message", "请求被限流");
            }
            result.put("timestamp", System.currentTimeMillis());

            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(result));
        };
    }
}
