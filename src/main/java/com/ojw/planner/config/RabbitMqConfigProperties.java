package com.ojw.planner.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqConfigProperties {

    private EntityConfig friend;

    @Data
    public static class EntityConfig {
        private String exchange;
        private Structure request;
    }

    @Data
    public static class Structure {
        private String routing;
    }

}