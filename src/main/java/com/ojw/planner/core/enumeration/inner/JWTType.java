package com.ojw.planner.core.enumeration.inner;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Key;

@Getter
@AllArgsConstructor
public enum JWTType {

    ACCESS(null, null),
    REFRESH(null, null);

    private Key key;

    private Long expire;

    public void setKey(Key key) {
        this.key = key;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

}
