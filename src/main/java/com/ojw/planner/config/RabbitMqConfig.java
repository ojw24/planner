package com.ojw.planner.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RabbitMqConfig {

    private final RabbitMqConfigProperties props;

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Exchange friendExchange() {
        return ExchangeBuilder.directExchange(props.getFriend().getExchange())
                .durable(true)
                .build();
    }

    @Bean
    public Exchange boardExchange() {
        return ExchangeBuilder.directExchange(props.getBoard().getExchange())
                .durable(true)
                .build();
    }

    @Bean
    public Exchange scheduleExchange() {
        return ExchangeBuilder.directExchange(props.getSchedule().getExchange())
                .durable(true)
                .build();
    }

}
