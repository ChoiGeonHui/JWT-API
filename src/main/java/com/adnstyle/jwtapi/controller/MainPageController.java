package com.adnstyle.jwtapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainPageController {


    @ResponseBody
    @RequestMapping("/main/list")
    public Map<String, String> list () {
        Map<String, String> result = new HashMap<>();

        result.put("result","메인 페이지");

        return result;
    }

    @ResponseBody
    @RequestMapping("/admin/list")
    public Map<String, String> adminList () {
        Map<String, String> result = new HashMap<>();

        result.put("result","관리자 페이지");

        return result;
    }


}
