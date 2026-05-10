package com.thinkboot.mq.rabbitmq.example;

import com.thinkboot.mq.rabbitmq.core.RabbitMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发送示例
 * 开发者只需关注业务逻辑，注入 RabbitMessageSender 即可发送消息
 */
@Service
public class ExampleMessageProducer {

    @Autowired
    private RabbitMessageSender messageSender;

    /**
     * 示例：发送普通消息
     */
    public void sendOrderCreatedMessage(Object orderData) {
        messageSender.send("order.created", orderData);
    }

    /**
     * 示例：发送延迟消息（30分钟后超时）
     */
    public void sendOrderTimeoutMessage(Object orderData) {
        messageSender.sendDelay("order.timeout", orderData, 30 * 60 * 1000L);
    }

    /**
     * 示例：发送到自定义交换机
     */
    public void sendToCustomExchange(String exchange, String routingKey, Object data) {
        messageSender.send(exchange, routingKey, data);
    }
}
