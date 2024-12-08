package com.ojw.planner.core.enumeration.inner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtPrefix {

    PREFIX("Bearer ");

    private final String type;

}
