package com.thinkboot.mq.rabbitmq;

import com.thinkboot.mq.rabbitmq.config.RabbitMQConfig;
import com.thinkboot.mq.rabbitmq.config.RabbitMQProperties;
import com.thinkboot.mq.rabbitmq.core.RabbitMessageSender;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(prefix = "thinkboot.mq.rabbitmq", name = "enable", havingValue = "true")
@EnableConfigurationProperties(RabbitMQProperties.class)
@Import(RabbitMQConfig.class)
@ComponentScan(basePackages = "com.thinkboot.mq.rabbitmq")
public class RabbitMQAutoConfiguration {
}
