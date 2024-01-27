package com.iot.greenhouse.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitClient {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    public void send(EventPayload eventPayload) {
        log.info("Sending event to the queue, event with name: {}", eventPayload.getEventName());
        rabbitTemplate.convertAndSend(exchange, routingkey, eventPayload);
    }
}