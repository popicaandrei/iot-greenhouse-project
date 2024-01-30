package com.iot.greenhouse.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    String host;
    @Value("${spring.rabbitmq.username}")
    String username;
    @Value("${spring.rabbitmq.password}")
    String password;
    @Value("${spring.rabbitmq.rbp.queue}")
    private String queue;
    @Value("${spring.rabbitmq.rbp.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.rbp.routingkey}")
    private String routingkey;
    @Value("${spring.rabbitmq.feedback.queue}")
    private String queueFeedback;
    @Value("${spring.rabbitmq.feedback.exchange}")
    private String exchangeFeedback;
    @Value("${spring.rabbitmq.feedback.routingkey}")
    private String routingkeyFeedback;

    @Bean
    Queue queue() {
        return new Queue(queue, true);
    }

    @Bean
    Queue queueFeedback() {
        return new Queue(queueFeedback, true);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public TopicExchange exchangeFeedback(){
        return new TopicExchange(exchangeFeedback);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingkey);
    }

    @Bean
    public Binding feedbackBinding(){
        return BindingBuilder
                .bind(queueFeedback())
                .to(exchangeFeedback())
                .with(routingkeyFeedback);
    }

    @Bean
    CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}