package com.springstudy.project.myweddingplanner.controller;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;
import com.springstudy.project.myweddingplanner.service.spec.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
@RequestMapping("login")
public class LoginController {

    private final LoginService loginService;

    @GetMapping("")
    public String login(){
        return "login";
    }

    @PostMapping("/form/success")
    public String success(MemberDTO member, HttpServletResponse response) {
        loginService.processFormLoginSuccess(member, response);
        return "redirect:/member/main";
    }

    @GetMapping("/oauth/success")
    public String success(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) {
        String userEmail = principal.getAttribute("email");
        String userName = principal.getAttribute("name");
        String defaultPwForOAuthUser = "default"; // oauth로 로그인 한 유저의 pw는 알 수 없으므로
        MemberDTO member = new MemberDTO(userEmail, userName, defaultPwForOAuthUser);

        loginService.processOAuthLoginSuccess(member, response);

        return "redirect:/member/main";
    }

    // csrf 토큰 받기 위해 get 대신 post
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String email = loginService.logout(request, response);
        System.out.println("logout email : "+email);

        return "/login";
    }
}
  