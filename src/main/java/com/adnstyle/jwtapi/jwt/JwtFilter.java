package com.adnstyle.jwtapi.jwt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;


    /**
     * 가지고 있는 토큰의 검사하는 메서드
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String path = ((HttpServletRequest) servletRequest).getServletPath();
        if (path.startsWith("/member/")||path.startsWith("/logout")||path.startsWith("/api/")){
            filterChain.doFilter(servletRequest,servletResponse);

        } else {

            String token = "";

            token = jwtProvider.resolveToken((HttpServletRequest) servletRequest);

            String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();

            HttpServletRequest request = (HttpServletRequest) servletRequest;

            if (StringUtils.hasText(token)&&jwtProvider.validateToken(token, request) ) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Security context에 인증 정보를 저장했습니다, uri: {" + requestURI + "}");
            } else {
                log.debug("유효한 Jwt 토큰이 없습니다, uri: {" + requestURI + "}");
            }


            filterChain.doFilter(servletRequest, servletResponse);
        }
    }


//    private void sendErrorResponse(HttpServletResponse response, String message) throws Exception {
//        response.setCharacterEncoding("utf-8");
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType(APPLICATION_JSON_VALUE);
//        response.getWriter().write(objectMapper.writeValueAsString(Response.builder()))
//
//        ;
//    }



}
