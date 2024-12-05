package com.ojw.planner.core.enumeration.converter;

import com.ojw.planner.core.enumeration.mapper.EnumMapper;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Objects;

public abstract class EnumConverter<T extends Enum<T> & EnumMapper> implements AttributeConverter<T, String> {
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    //enum to db column값으로 변경
    @Override
    public String convertToDatabaseColumn(T attribute) {
        if (Objects.isNull(attribute)) return null;
        return attribute.getCode();
    }

    //db column값 to enum으로 변경
    @Override
    public T convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData)) return null;
        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> e.getCode().equals(dbData))
                .findFirst()
                .orElse(null);
    }

}
