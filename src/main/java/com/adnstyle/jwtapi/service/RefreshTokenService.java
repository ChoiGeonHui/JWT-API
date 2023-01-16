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


    //로그인 시 생성되는 refresh token
    public int updateToken(RefreshTokenList refreshTokenList){
        return refreshTokenRepository.updateToken(refreshTokenList);
    }

    //access token 재발급 메서드
    public String updateAccessToken(HttpServletResponse response, String refreshToken) {


        // refresh 존재 여부
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            return "유효하지 않는 refreshToken 입니다. 다시 로그인 하세요.";
        }

        Claims claims = jwtProvider.parseClaims(refreshToken);
        RefreshTokenList refreshTokenList = new RefreshTokenList();

        refreshTokenList.setId(claims.getSubject());
        refreshTokenList.setRefresh(refreshToken);

        RefreshTokenList refreshTokenList2 = refreshTokenRepository.selectToken2(refreshTokenList);

        if (refreshTokenList2 == null) {
            return "저장된 refreshToken이 다릅니다. \n 재로그인 후 같은 오류 발생시 문의 주세요.";
        }

        if (jwtProvider.validateRefreshToken(refreshTokenList2.getAccess())){
            return "아직 만료가 안된 AccessToken 입니다. 혹시 refreshToken을 탈취당했습니까?";
        }


        String newAccessToken = jwtProvider.createToken(claims.getSubject(),(String) claims.get("role"));
        refreshTokenList2.setAccess(newAccessToken);
        updateToken(refreshTokenList2);
        response.setHeader("Authorization", newAccessToken);

        return "access Token 재발급 완료 : "+newAccessToken ;
    }





}
