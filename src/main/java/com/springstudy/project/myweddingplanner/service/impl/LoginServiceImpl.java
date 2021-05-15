package com.springstudy.project.myweddingplanner.service.impl;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;
import com.springstudy.project.myweddingplanner.repository.MemberRepository;
import com.springstudy.project.myweddingplanner.security.jwt.JwtProvider;
import com.springstudy.project.myweddingplanner.service.spec.LoginService;
import com.springstudy.project.myweddingplanner.service.spec.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    // private final MemberRepository memberRepository;

    @Override
    public void processWhenLoginSuccess(MemberDTO member, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", member.getName());

        String jwt = jwtProvider.createToken(member.getEmail(), params);

        if(jwt!=null) {
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setPath("/");
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
        }

        memberService.register(member);
    }
}
