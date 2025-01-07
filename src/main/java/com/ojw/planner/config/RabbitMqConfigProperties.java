package com.ojw.planner.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqConfigProperties {

    private EntityConfig friend;

    private EntityConfig board;

    private EntityConfig schedule;

    @Data
    public static class EntityConfig {
        private String exchange;
        private Map<String, Structure> routes;
    }

    @Data
    public static class Structure {
        private String routing;
    }

}