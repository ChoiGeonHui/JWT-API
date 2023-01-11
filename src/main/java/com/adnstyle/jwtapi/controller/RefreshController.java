package com.adnstyle.jwtapi.controller;


import com.adnstyle.jwtapi.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshTokenService refreshTokenService;


    @RequestMapping("/refresh")
    public Map<String, String> refresh(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> result = new HashMap<>();

        String refreshToken = request.getHeader("RTK");

        result.put("result",refreshTokenService.updateAccessToken(response, refreshToken));

        return result;
    }





}
