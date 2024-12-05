package com.ojw.planner.core.enumeration.inner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtClaim {

    ID("id"),
    NAME("name");

    private final String type;

}
