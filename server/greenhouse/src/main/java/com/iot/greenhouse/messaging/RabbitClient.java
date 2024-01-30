package com.iot.greenhouse.messaging;

import com.iot.greenhouse.model.CommandLog;
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

    @Value("${spring.rabbitmq.feedback.exchange}")
    private String exchangeFeedback;

    @Value("${spring.rabbitmq.feedback.routingkey}")
    private String routingkeyFeedback;

    public void sendToFeedbackTopic(CommandLog eventPayload) {
        log.info("Sending event to the feedback queue, event with switch state {}", eventPayload.isHeaterSwitch());
        rabbitTemplate.convertAndSend(exchangeFeedback, routingkeyFeedback, eventPayload);
    }
}