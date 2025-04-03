package com.ojw.planner.app.system.common.service;

import com.ojw.planner.app.system.common.dto.mq.Setting;
import com.ojw.planner.config.RabbitMqConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommonService {

    @Value("${spring.rabbitmq.host}")
    private String mqHost;

    @Value("${spring.rabbitmq.stomp}")
    private Integer mqPort;

    @Value("${spring.rabbitmq.username}")
    private String mqUser;

    @Value("${spring.rabbitmq.password}")
    private String mqPassword;

    private final RabbitMqConfigProperties rabbitMqProp;

    private final RabbitAdmin rabbitAdmin;

    /**
     * MQ 설정 조회
     * 
     * @return MQ 설정 정보
     */
    public Setting getMqConfig() throws Exception {
        return Setting.builder()
                .host(mqHost)
                .port(mqPort)
                .user(mqUser)
                .password(mqPassword)
                .build()
                .configs(rabbitMqProp);
    }

    /**
     * MQ Queue 삭제
     *
     * @param name - Queue 이름
     */
    public void deleteQueue(String name) {
        rabbitAdmin.deleteQueue(name);
    }

}
