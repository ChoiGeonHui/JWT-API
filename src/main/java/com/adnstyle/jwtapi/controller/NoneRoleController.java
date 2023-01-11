package com.adnstyle.jwtapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/access")
public class NoneRoleController {

    @RequestMapping("/no-role")
    public String accessRole () {
        return "접근 권한이 없습니다.";
    }
}
