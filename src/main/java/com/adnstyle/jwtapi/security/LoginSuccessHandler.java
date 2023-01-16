package com.adnstyle.jwtapi.security;

import com.adnstyle.jwtapi.common.MemberDetail;
import com.adnstyle.jwtapi.domain.RefreshTokenList;
import com.adnstyle.jwtapi.jwt.JwtProvider;
import com.adnstyle.jwtapi.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {


    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        MemberDetail ghMember = (MemberDetail) authentication.getPrincipal();

        String accessToken = jwtProvider.createToken(ghMember.getGhMember().getId(), ghMember.getGhMember().getRole());//accessToken 생성
        String refreshToken = jwtProvider.createRefreshToken(ghMember.getGhMember().getId(), ghMember.getGhMember().getRole()); // refreshToken 생성

        setHeaderAccessToken(response, accessToken); //access token 헤더에 추가
        setHeaderRefreshToken(response, refreshToken); // refresh token 헤더에 추가

        RefreshTokenList refreshTokenList = new RefreshTokenList();
        refreshTokenList.setRefresh(refreshToken);
        refreshTokenList.setAccess(accessToken);
        refreshTokenList.setId(ghMember.getGhMember().getId());

        refreshTokenService.updateToken(refreshTokenList); //생성된 refresh token DB에 저장

        log.debug("AccessToken : "+ accessToken);
        log.debug("RefreshToken : "+ refreshToken);

        response.sendRedirect("/member/success");
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Authorization", accessToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String RefreshToken) {
        response.setHeader("RTK", RefreshToken);
    }
}
