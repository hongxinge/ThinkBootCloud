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

    /**
     * 发送消息到默认交换机
     * 
     * @param routingKey 路由键
     * @param message 消息内容（会自动序列化为JSON）
     */
    public void send(String routingKey, Object message) {
        try {
            rabbitTemplate.convertAndSend(properties.getExchange(), routingKey, message);
            log.info("[RabbitMQ] 发送消息成功: exchange={}, routingKey={}", properties.getExchange(), routingKey);
        } catch (AmqpException e) {
            log.error("[RabbitMQ] 发送消息失败: exchange={}, routingKey={}", properties.getExchange(), routingKey, e);
            throw e;
        }
    }

    /**
     * 发送延迟消息
     * 
     * @param routingKey 路由键
     * @param message 消息内容
     * @param delayMillis 延迟时间（毫秒）
     */
    public void sendDelay(String routingKey, Object message, long delayMillis) {
        try {
            rabbitTemplate.convertAndSend(
                    properties.getExchange(),
                    routingKey,
                    message,
                    (MessagePostProcessor) msg -> {
                        msg.getMessageProperties().setDelay((int) Math.min(delayMillis, Integer.MAX_VALUE));
                        return msg;
                    }
            );
            log.info("[RabbitMQ] 发送延迟消息成功: exchange={}, routingKey={}, delay={}ms", 
                    properties.getExchange(), routingKey, delayMillis);
        } catch (AmqpException e) {
            log.error("[RabbitMQ] 发送延迟消息失败: exchange={}, routingKey={}", properties.getExchange(), routingKey, e);
            throw e;
        }
    }

    /**
     * 发送消息到指定交换机
     * 
     * @param exchange 交换机名称
     * @param routingKey 路由键
     * @param message 消息内容
     */
    public void send(String exchange, String routingKey, Object message) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("[RabbitMQ] 发送消息成功: exchange={}, routingKey={}", exchange, routingKey);
        } catch (AmqpException e) {
            log.error("[RabbitMQ] 发送消息失败: exchange={}, routingKey={}", exchange, routingKey, e);
            throw e;
        }
    }
}
