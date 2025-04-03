package com.ojw.planner.app.system.common.dto.mq;

import com.ojw.planner.config.RabbitMqConfigProperties;
import com.ojw.planner.config.RabbitMqConfigProperties.EntityConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Setting {

    private String host;

    private Integer port;

    private String user;

    private String password;

    @Schema(description = "설정 리스트")
    private List<Config> configs;

    public Setting configs(RabbitMqConfigProperties props) throws Exception {
        this.configs = getConfigs(props);
        return this;
    }

    public static Setting of(RabbitMqConfigProperties props, String host, Integer port) throws Exception {
        return Setting.builder()
                .host(host)
                .port(port)
                .configs(getConfigs(props))
                .build();
    }

    private static List<Config> getConfigs(RabbitMqConfigProperties props) throws IllegalAccessException {

        List<Config> configs = new ArrayList<>();
        Field[] fields = RabbitMqConfigProperties.class.getDeclaredFields();
        for (Field field : fields) {

            if(field.getType().equals(EntityConfig.class)) {

                field.setAccessible(true);

                configs.add(
                        Config.builder()
                                .name(field.getName())
                                .config((EntityConfig) field.get(props))
                                .build()
                );

            }

        }

        return configs;

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config {

        private String name;

        private EntityConfig config;

    }

}
