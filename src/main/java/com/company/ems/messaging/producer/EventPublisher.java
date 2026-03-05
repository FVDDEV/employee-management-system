package com.company.ems.messaging.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(
            String exchange,
            String routingKey,
            Object event) {

        log.info("Publishing event | exchange={} | key={} | payload={}",
                exchange, routingKey, event);

        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
