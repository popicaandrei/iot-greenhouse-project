package com.iot.greenhouse.messaging;


import com.iot.greenhouse.service.GreenhouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitConsumer {
    public final GreenhouseService greenhouseService;

    @RabbitListener(queues = {"${spring.rabbitmq.rbp.queue}"})
    public void receive(Message<EventPayload> message) {
        try {
            log.info("New Greenhouse event received with temperature: " + message.getPayload().getTemperature());
            greenhouseService.interpretMonitorData(message.getPayload());
        } catch (Exception ex) {
            throw new RuntimeException("Event listener error: " + ex);
        }
    }
}