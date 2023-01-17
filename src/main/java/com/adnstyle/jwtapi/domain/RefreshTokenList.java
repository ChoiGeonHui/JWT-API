package com.adnstyle.jwtapi.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Alias("refreshTokenList")
@Data
@NoArgsConstructor
public class RefreshTokenList {

    private Long seq; //식별자

    private String id; //아이디

    private String refresh;

    private String access;

}
