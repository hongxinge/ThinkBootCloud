package com.thinkboot.mq.rabbitmq.core;

import com.thinkboot.mq.rabbitmq.config.RabbitMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "thinkboot.mq.rabbitmq", name = "enable", havingValue = "true")
public class RabbitMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQProperties properties;

    public void send(String routingKey, Object message) {
        try {
            rabbitTemplate.convertAndSend(properties.getExchange(), routingKey, message);
            log.info("发送消息成功: exchange={}, routingKey={}, message={}", properties.getExchange(), routingKey, message);
        } catch (AmqpException e) {
            log.error("发送消息失败: exchange={}, routingKey={}", properties.getExchange(), routingKey, e);
            throw e;
        }
    }

    public void sendDelay(String routingKey, Object message, long delayMillis) {
        try {
            rabbitTemplate.convertAndSend(
                    properties.getExchange(),
                    routingKey,
                    message,
                    (MessagePostProcessor) msg -> {
                        msg.getMessageProperties().setDelay((int) delayMillis);
                        return msg;
                    }
            );
            log.info("发送延迟消息成功: exchange={}, routingKey={}, delay={}ms, message={}",
                    properties.getExchange(), routingKey, delayMillis, message);
        } catch (AmqpException e) {
            log.error("发送延迟消息失败: exchange={}, routingKey={}", properties.getExchange(), routingKey, e);
            throw e;
        }
    }
}
