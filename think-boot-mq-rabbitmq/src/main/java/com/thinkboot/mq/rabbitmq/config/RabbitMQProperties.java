package com.thinkboot.mq.rabbitmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "thinkboot.mq.rabbitmq")
public class RabbitMQProperties {

    private boolean enable = false;

    private String exchange = "thinkboot.default.exchange";

    private String queuePrefix = "thinkboot.";

    private boolean enableDelayMessage = false;

    private int delayMessageTtl = 60000;
}
