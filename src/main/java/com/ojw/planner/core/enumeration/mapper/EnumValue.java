package com.ojw.planner.core.enumeration.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EnumValue {

    private String code;

    private String description;

    public static EnumValue toEnumValue(Class<? extends EnumMapper> e, String code) {
        return Arrays.stream(e.getEnumConstants())
                .filter(enumConstant -> enumConstant.getCode().equals(code))
                .findFirst()
                .map(ee -> EnumValue.builder()
                        .code(ee.getCode())
                        .description(ee.getDescription())
                        .build()
                )
                .orElse(null);
    }

}
