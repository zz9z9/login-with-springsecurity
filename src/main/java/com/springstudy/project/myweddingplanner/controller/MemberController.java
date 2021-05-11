package com.springstudy.project.myweddingplanner.controller;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;
import com.springstudy.project.myweddingplanner.service.spec.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final MemberService memberService;

    @GetMapping("/main")
    public String userMain() {
        return "main";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup/complete")
    public String signupComplete(@ModelAttribute MemberDTO member) {
        String result = memberService.register(member);
        return "redirect:/login";
    }
}