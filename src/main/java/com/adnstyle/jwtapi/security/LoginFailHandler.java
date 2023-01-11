package com.adnstyle.jwtapi.security;

import com.adnstyle.jwtapi.domain.GhMember;
import com.adnstyle.jwtapi.service.GhMemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter
@Setter
@Component
@Transactional
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

    private final GhMemberService ghMemberService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String id = request.getParameter("id");
        GhMember ghMember = new GhMember();
        ghMember.setId(id);

        ghMember = ghMemberService.selectMember(ghMember);

        if (ghMember == null) {
            String errorMsg = "아이디 또는 비밀번호가 틀렸습니다.";
            request.setAttribute("errorMsg", errorMsg);
            request.getRequestDispatcher("/member/fail").forward(request, response);
            return;
        }


        String errorMsg = "";

        if (ghMember.getLockYN().equals("Y")) {
            errorMsg = " 해당 아이디로 로그인 5회 이상 실패하여 계정이 비활성화 되었습니다.\n 관리자에게 문의 주세요.";
        } else if (ghMember.getLockYN().equals("N")) {
            errorMsg = "아이디 또는 비밀번호가 틀렸습니다.";
        } else {
            errorMsg = "네트워크가 불안정 합니다.";
        }

        request.setAttribute("errorMsg", errorMsg);
        request.getRequestDispatcher("/member/fail").forward(request, response);


    }



}
