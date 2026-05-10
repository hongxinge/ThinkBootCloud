package com.thinkboot.mq.rabbitmq.config;

import com.thinkboot.mq.rabbitmq.config.RabbitMQProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "thinkboot.mq.rabbitmq", name = "enable", havingValue = "true")
public class RabbitMQConfig {

    @Autowired
    private RabbitMQProperties properties;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public DirectExchange defaultDirectExchange() {
        return new DirectExchange(properties.getExchange(), true, false);
    }

    @Bean
    public Queue defaultQueue() {
        return QueueBuilder.durable(properties.getQueuePrefix() + "default.queue")
                .withArgument("x-dead-letter-exchange", properties.getExchange() + ".dlx")
                .withArgument("x-dead-letter-routing-key", "dlx.default.routing")
                .build();
    }

    @Bean
    public Binding defaultBinding(Queue defaultQueue, DirectExchange defaultDirectExchange) {
        return BindingBuilder.bind(defaultQueue)
                .to(defaultDirectExchange)
                .with(properties.getQueuePrefix() + "default.routing");
    }
}
