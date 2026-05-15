package com.thinkboot.sentinel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Sentinel 配置属性类
 * 使用 Spring Cloud Alibaba 原生 spring.cloud.alibaba.sentinel.* 配置
 * 框架仅增强原生没有的功能（如 Nacos 数据源配置）
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.cloud.alibaba.sentinel")
public class SentinelProperties {
    /**
     * 是否启用 Sentinel（原生配置）
     */
    private boolean enabled = true;
    
    /**
     * Sentinel Dashboard 地址（原生配置）
     * 示例：localhost:8080
     */
    private String dashboard = "localhost:8080";
    
    /**
     * 是否立即初始化（原生配置）
     */
    private boolean eager = false;
    
    /**
     * 框架增强：Nacos 数据源配置（原生没有的便捷配置）
     */
    private Datasource datasource = new Datasource();

    @Data
    public static class Datasource {
        private Nacos nacos = new Nacos();
    }

    @Data
    public static class Nacos {
        /**
         * 是否启用 Nacos 数据源
         */
        private boolean enabled = false;
        
        /**
         * Nacos 服务器地址
         */
        private String serverAddr;
        
        /**
         * 命名空间
         */
        private String namespace;
        
        /**
         * 分组 ID
         */
        private String groupId = "SENTINEL_GROUP";
        
        /**
         * 数据 ID
         */
        private String dataId;
        
        /**
         * 规则类型：flow（流控）、degrade（降级）、system（系统）、authority（授权）
         */
        private String ruleType = "flow";
    }
}
