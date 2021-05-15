package com.springstudy.project.myweddingplanner.controller;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;
import com.springstudy.project.myweddingplanner.service.spec.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @GetMapping("")
    public String login(){
        return "login";
    }

    @GetMapping("/form/success")
    public String success(@RequestBody Map<String, String> params, HttpServletResponse response) {
        String userEmail = params.get("email");
        String userName = params.get("name");
        String userPw = params.get("password");
        MemberDTO member = new MemberDTO(userEmail, userName, userPw);

        loginService.processWhenLoginSuccess(member, response);

        return "redirect:/member/main";
    }

    @GetMapping("/oauth/success")
    public String success(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) {
        String userEmail = principal.getAttribute("email");
        String userName = principal.getAttribute("name");
        String defaultPwForOAuthUser = "default"; // oauth로 로그인 한 유저의 pw는 알 수 없으므로
        MemberDTO member = new MemberDTO(userEmail, userName, defaultPwForOAuthUser);

        loginService.processWhenLoginSuccess(member, response);

        return "redirect:/member/main";
    }

}
  