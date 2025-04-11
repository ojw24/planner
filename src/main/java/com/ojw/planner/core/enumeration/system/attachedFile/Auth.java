package com.ojw.planner.core.enumeration.system.attachedFile;

import com.ojw.planner.core.enumeration.converter.EnumConverter;
import com.ojw.planner.core.enumeration.mapper.EnumMapper;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Auth implements EnumMapper {

    PUBLIC("auth_001", "public"),
    PRIVATE("auth_002", "private");

    private final String code;

    private final String description;

    @Converter(autoApply = true)
    static class AuthConverter extends EnumConverter<Auth> {
        public AuthConverter() {
            super(Auth.class);
        }
    }

}
