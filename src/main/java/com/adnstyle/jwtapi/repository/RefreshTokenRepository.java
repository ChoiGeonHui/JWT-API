package com.adnstyle.jwtapi.repository;

import com.adnstyle.jwtapi.domain.RefreshTokenList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenRepository {

    RefreshTokenList selectToken(String id);

    RefreshTokenList selectToken2(RefreshTokenList refreshTokenList);

    int updateToken(RefreshTokenList refreshTokenList);


}
