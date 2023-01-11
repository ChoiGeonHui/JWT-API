package com.adnstyle.jwtapi.service;

import com.adnstyle.jwtapi.domain.RefreshTokenList;
import com.adnstyle.jwtapi.jwt.JwtProvider;
import com.adnstyle.jwtapi.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtProvider jwtProvider;


    public RefreshTokenList selectToken(String id) {
        return refreshTokenRepository.selectToken(id);
    }

    public int updateToken(RefreshTokenList refreshTokenList){
        return refreshTokenRepository.updateToken(refreshTokenList);
    }

    public String updateAccessToken(HttpServletResponse response, String refreshToken) {


        if (!jwtProvider.validateRefreshToken(refreshToken)) {

            return "유효하지 않는 refreshToken 입니다. 다시 로그인 하세요.";

        }

        Claims claims = jwtProvider.parseClaims(refreshToken);
        RefreshTokenList refreshTokenList = new RefreshTokenList();

        refreshTokenList.setId(claims.getSubject());
        refreshTokenList.setToken(refreshToken);

        RefreshTokenList refreshTokenList2 = refreshTokenRepository.selectToken2(refreshTokenList);

        if (refreshTokenList2 == null) {
            return "잘못된 refreshToken 입니다.";
        }

        String newAccessToken = jwtProvider.createToken(claims.getSubject(),(String) claims.get("role"));

        response.setHeader("Authorization", newAccessToken);

        return "access Token 재발급 완료 : "+newAccessToken ;
    }





}
