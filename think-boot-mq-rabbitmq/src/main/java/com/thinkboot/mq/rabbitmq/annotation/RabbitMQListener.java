package com.thinkboot.mq.rabbitmq.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbitMQListener {

    String queues();

    String exchange() default "";

    String routingKey() default "";

    boolean ackMode() default true;
}
