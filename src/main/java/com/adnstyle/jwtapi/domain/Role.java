package com.adnstyle.jwtapi.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    SOCIAL("ROLE_SOCIAL","소셜사용자"),
    USER("ROLE_USER","일반사용자"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String title;

}
