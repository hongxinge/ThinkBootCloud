package com.thinkboot.mq.rabbitmq.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费示例
 * 开发者只需关注业务逻辑，使用 @RabbitListener 即可消费消息
 */
@Slf4j
@Component
public class ExampleMessageConsumer {

    /**
     * 示例：消费订单创建消息
     */
    @RabbitListener(queues = "thinkboot.order.created")
    public void handleOrderCreated(Object orderData) {
        log.info("收到订单创建消息: {}", orderData);
        // 处理订单创建后的业务逻辑
    }

    /**
     * 示例：消费订单超时消息
     */
    @RabbitListener(queues = "thinkboot.order.timeout")
    public void handleOrderTimeout(Object orderData) {
        log.info("收到订单超时消息: {}", orderData);
        // 处理订单超时取消业务逻辑
    }
}
