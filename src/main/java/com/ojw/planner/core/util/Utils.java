package com.ojw.planner.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Utils {

    public static LocalDateTime now() {
        return LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

}
