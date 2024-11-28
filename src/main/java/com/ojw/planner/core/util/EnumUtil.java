package com.ojw.planner.core.util;

import com.ojw.planner.core.enumeration.mapper.EnumMapper;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public final class EnumUtil {

    public static <T extends Enum<T> & EnumMapper> T ofCode(Class<T> base, String code) {
        return StringUtils.hasText(code)
                ? Arrays.stream(base.getEnumConstants())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(base.getSimpleName() + "의 code 값이 올바르지 않습니다.\n입력 값 : %s", code)
                ))
                : null;
    }

}
