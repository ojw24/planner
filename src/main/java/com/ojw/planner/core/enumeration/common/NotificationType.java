package com.ojw.planner.core.enumeration.common;

import com.ojw.planner.core.enumeration.converter.EnumConverter;
import com.ojw.planner.core.enumeration.mapper.EnumMapper;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType implements EnumMapper {

    REQUEST("noti_type_001", "request"),
    APPROVE("noti_type_002","approve"),
    REJECT("noti_type_003","reject");

    private final String code;

    private final String description;

    @Converter(autoApply = true)
    static class NotificationTypeConverter extends EnumConverter<NotificationType> {
        public NotificationTypeConverter() {
            super(NotificationType.class);
        }
    }

}
