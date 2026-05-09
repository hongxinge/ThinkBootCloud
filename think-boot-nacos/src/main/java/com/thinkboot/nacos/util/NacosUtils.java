package com.thinkboot.nacos.util;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class NacosUtils {

    private NacosUtils() {
    }

    public static NamingService createNamingService(String serverAddr) {
        try {
            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverAddr);
            return NamingFactory.createNamingService(properties);
        } catch (NacosException e) {
            throw new RuntimeException("Failed to create Nacos naming service", e);
        }
    }

    public static NamingService createNamingService(String serverAddr, String namespace) {
        try {
            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverAddr);
            properties.setProperty(PropertyKeyConst.NAMESPACE, namespace);
            return NamingFactory.createNamingService(properties);
        } catch (NacosException e) {
            throw new RuntimeException("Failed to create Nacos naming service", e);
        }
    }

    public static ConfigService createConfigService(String serverAddr) {
        try {
            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverAddr);
            return ConfigFactory.createConfigService(properties);
        } catch (NacosException e) {
            throw new RuntimeException("Failed to create Nacos config service", e);
        }
    }

    public static void registerInstance(NamingService namingService, String serviceName, String ip, int port) {
        try {
            Instance instance = new Instance();
            instance.setIp(ip);
            instance.setPort(port);
            instance.setServiceName(serviceName);
            instance.setHealthy(true);
            instance.setWeight(1.0);
            namingService.registerInstance(serviceName, instance);
            log.info("Registered instance: {}:{} to service: {}", ip, port, serviceName);
        } catch (NacosException e) {
            throw new RuntimeException("Failed to register instance", e);
        }
    }

    public static void deregisterInstance(NamingService namingService, String serviceName, String ip, int port) {
        try {
            namingService.deregisterInstance(serviceName, ip, port);
            log.info("Deregistered instance: {}:{} from service: {}", ip, port, serviceName);
        } catch (NacosException e) {
            throw new RuntimeException("Failed to deregister instance", e);
        }
    }

    public static List<Instance> getHealthyInstances(NamingService namingService, String serviceName) {
        try {
            return namingService.selectInstances(serviceName, true);
        } catch (NacosException e) {
            throw new RuntimeException("Failed to get healthy instances", e);
        }
    }

    public static List<String> getHealthyInstanceIps(NamingService namingService, String serviceName) {
        return getHealthyInstances(namingService, serviceName)
                .stream()
                .map(Instance::getIp)
                .collect(Collectors.toList());
    }

    public static String getConfig(ConfigService configService, String dataId, String group) {
        try {
            return configService.getConfig(dataId, group, 5000);
        } catch (NacosException e) {
            throw new RuntimeException("Failed to get config: " + dataId, e);
        }
    }

    public static boolean publishConfig(ConfigService configService, String dataId, String group, String content) {
        try {
            return configService.publishConfig(dataId, group, content);
        } catch (NacosException e) {
            throw new RuntimeException("Failed to publish config: " + dataId, e);
        }
    }

    public static boolean removeConfig(ConfigService configService, String dataId, String group) {
        try {
            return configService.removeConfig(dataId, group);
        } catch (NacosException e) {
            throw new RuntimeException("Failed to remove config: " + dataId, e);
        }
    }

    public static boolean isServiceHealthy(NamingService namingService, String serviceName) {
        try {
            List<Instance> instances = namingService.selectInstances(serviceName, true);
            return !instances.isEmpty();
        } catch (NacosException e) {
            log.error("Failed to check service health: {}", serviceName, e);
            return false;
        }
    }
}
