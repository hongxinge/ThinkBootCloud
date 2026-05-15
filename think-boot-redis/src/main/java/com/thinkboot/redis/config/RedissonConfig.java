package com.thinkboot.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Redisson 配置类
 * 使用 Spring Boot 原生 spring.redis.* 配置，降低开发者学习成本
 * 支持单节点、集群、哨兵三种模式
 */
@Configuration
public class RedissonConfig {

    // 从 spring.redis.* 原生配置读取
    @Value("${spring.redis.host:127.0.0.1}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private int port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.database:0}")
    private int database;

    @Value("${spring.redis.sentinel.master:}")
    private String masterName;

    // 框架增强的 Redisson 高级配置（原生没有的）
    @Value("${thinkboot.redis.redisson.mode:single}")
    private String mode;

    @Value("${thinkboot.redis.redisson.connection-pool-size:64}")
    private int connectionPoolSize;

    @Value("${thinkboot.redis.redisson.connection-minimum-idle-size:10}")
    private int connectionMinimumIdleSize;

    @Value("${thinkboot.redis.redisson.timeout:3000}")
    private int timeout;

    @Value("${thinkboot.redis.redisson.cluster.node-addresses:}")
    private List<String> nodeAddresses;

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "thinkboot.redis.redisson", name = "mode", havingValue = "single", matchIfMissing = true)
    public RedissonClient redissonClientSingle() {
        String address = "redis://" + host + ":" + port;
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(address)
                .setDatabase(database)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setTimeout(timeout);

        if (password != null && !password.isEmpty()) {
            serverConfig.setPassword(password);
        }

        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "thinkboot.redis.redisson", name = "mode", havingValue = "cluster")
    public RedissonClient redissonClientCluster() {
        if (nodeAddresses == null || nodeAddresses.isEmpty()) {
            throw new IllegalStateException("Cluster mode requires thinkboot.redis.redisson.cluster.node-addresses configuration");
        }
        
        Config config = new Config();
        ClusterServersConfig serverConfig = config.useClusterServers()
                .addNodeAddress(nodeAddresses.toArray(new String[0]))
                .setScanInterval(2000)
                .setMasterConnectionPoolSize(connectionPoolSize)
                .setMasterConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setTimeout(timeout);

        if (password != null && !password.isEmpty()) {
            serverConfig.setPassword(password);
        }

        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "thinkboot.redis.redisson", name = "mode", havingValue = "sentinel")
    public RedissonClient redissonClientSentinel() {
        if (masterName == null || masterName.isEmpty()) {
            throw new IllegalStateException("Sentinel mode requires spring.redis.sentinel.master configuration");
        }
        if (nodeAddresses == null || nodeAddresses.isEmpty()) {
            throw new IllegalStateException("Sentinel mode requires thinkboot.redis.redisson.cluster.node-addresses configuration");
        }
        
        Config config = new Config();
        SentinelServersConfig serverConfig = config.useSentinelServers()
                .setMasterName(masterName)
                .addSentinelAddress(nodeAddresses.toArray(new String[0]))
                .setMasterConnectionPoolSize(connectionPoolSize)
                .setMasterConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setTimeout(timeout);

        if (password != null && !password.isEmpty()) {
            serverConfig.setPassword(password);
        }

        return Redisson.create(config);
    }
}
