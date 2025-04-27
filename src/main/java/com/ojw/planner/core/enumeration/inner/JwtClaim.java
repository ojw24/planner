package com.ojw.planner.core.enumeration.inner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtClaim {

    ID("id"),
    NAME("name"),
    UUID("uuid");

    private final String type;

}
