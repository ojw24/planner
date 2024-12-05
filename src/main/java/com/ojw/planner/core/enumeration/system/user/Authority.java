package com.ojw.planner.core.enumeration.system.user;

import com.ojw.planner.core.enumeration.converter.EnumConverter;
import com.ojw.planner.core.enumeration.mapper.EnumMapper;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Authority implements EnumMapper {

    ADMIN("authority_001", "ROLE_ADMIN"),
    USER("authority_002","ROLE_USER");

    private final String code;

    private final String description;

    @Converter(autoApply = true)
    static class AuthorityConverter extends EnumConverter<Authority> {
        public AuthorityConverter() {
            super(Authority.class);
        }
    }

}
