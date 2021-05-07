package com.springstudy.project.myweddingplanner.controller;

import com.springstudy.project.myweddingplanner.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController {

    private final JwtProvider jwtProvider;

    @GetMapping("")
    public String login(){
        return "login";
    }

    @GetMapping("/success")
    public String success(@RequestBody Map<String, String> params, HttpServletResponse response) {

        return "redirect:/member/main";
    }

    @GetMapping("/oauth/success")
    public String success(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        String jwt = jwtProvider.createToken(email, params);

        if(jwt!=null) {
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setPath("/");
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
        }

        return "redirect:/member/main";
    }
}
  