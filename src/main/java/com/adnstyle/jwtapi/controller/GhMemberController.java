package com.adnstyle.jwtapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class GhMemberController {



    @ResponseBody
    @RequestMapping("/login")
    public String login () {
        Map<String, String> result = new HashMap<>();

        return "로그인 페이지";
    }

    @ResponseBody
    @RequestMapping("/success")
    public String loginSuccess() {
        return "login success";
    }
    @ResponseBody
    @RequestMapping("/fail")
    public String loginFail() {
        return "login fail";
    }





}
