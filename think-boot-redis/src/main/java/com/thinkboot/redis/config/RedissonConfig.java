package com.thinkboot.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "thinkboot.redis.redisson")
public class RedissonConfig {

    private String mode = "single";

    private String address;

    private String password;

    private int database = 0;

    private int connectionPoolSize = 64;

    private int connectionMinimumIdleSize = 10;

    private int timeout = 3000;

    private List<String> nodeAddresses;

    private String masterName;

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "thinkboot.redis.redisson", name = "mode", havingValue = "single", matchIfMissing = true)
    public RedissonClient redissonClientSingle() {
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

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public void setConnectionMinimumIdleSize(int connectionMinimumIdleSize) {
        this.connectionMinimumIdleSize = connectionMinimumIdleSize;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setNodeAddresses(List<String> nodeAddresses) {
        this.nodeAddresses = nodeAddresses;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }
}
