package com.ojw.planner.core.util;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RabbitMqUtil {

    @Value("${spring.rabbitmq.host}")
    private String mqHost;

    @Value("${spring.rabbitmq.stomp}")
    private Integer mqPort;

    @Value("${spring.rabbitmq.username}")
    private String mqUser;

    @Value("${spring.rabbitmq.password}")
    private String mqPassword;

    private final RabbitAdmin rabbitAdmin;

    private boolean isExist(String name) {

        String url = "http://" + mqHost + ":" + mqPort + "/api/exchanges/%2F/" + name;

        try {

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(mqUser, mqPassword); // RabbitMQ 기본 계정

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), String.class
            );

            return response.getStatusCode() == HttpStatus.OK;

        } catch (Exception e) {
            return false;
        }

    }

    private Exchange declareExchange(String name) {

        Exchange exchange = ExchangeBuilder.fanoutExchange(name)
                .durable(true)
                .build();
        if(isExist(name)) return exchange;

        rabbitAdmin.declareExchange(exchange);
        return exchange;

    }

    private Queue declareQueue(String name) {

        Queue queue = QueueBuilder
                .durable(name)
                .autoDelete()
                .build();
        if(rabbitAdmin.getQueueProperties(name) != null) return queue;

        rabbitAdmin.declareQueue(queue);
        return queue;

    }

    public void declareBinding(String exchange, String queue) {
        rabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(declareQueue(queue))
                        .to(declareExchange(exchange))
                        .with("")
                        .noargs()
        );
    }

}
