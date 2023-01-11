package com.adnstyle.jwtapi.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        String exception = request.getAttribute("exception").toString();

        /**
         * 토큰 없는 경우
         */
        if (exception == null) {
            setResponse(response, ErrorCode.UNKNOWN_ERROR);
            return;
        }


        if (exception == null) {
            setResponse(response, ErrorCode.UNKNOWN_ERROR);
        }

        //잘못된 타입의 토큰인 경우
        else if (exception.equals(ErrorCode.WRONG_TYPE_TOKEN.getCode() + "")) {

            setResponse(response, ErrorCode.WRONG_TYPE_TOKEN);
        }

        //토큰 만료된 경우
        else if (exception.equals(ErrorCode.EXPIRED_TOKEN.getCode() + "")) {

            setResponse(response, ErrorCode.EXPIRED_TOKEN);
        }

        //지원되지 않는 토큰인 경우
        else if (exception.equals(ErrorCode.UNSUPPORTED_TOKEN.getCode() + "")) {

            setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);

        } else {

            setResponse(response, ErrorCode.ACCESS_DENIED);
        }
    }

    /**
     * 한글 출력을 위해 getWriter() 사용
     */
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        try {
            responseJson.put("message", errorCode.getMessage());
            responseJson.put("code", errorCode.getCode());
            response.getWriter().print(responseJson);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

}