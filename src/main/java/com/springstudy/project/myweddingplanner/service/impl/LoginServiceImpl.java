package com.springstudy.project.myweddingplanner.service.impl;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;
import com.springstudy.project.myweddingplanner.security.jwt.JwtProvider;
import com.springstudy.project.myweddingplanner.service.spec.LoginService;
import com.springstudy.project.myweddingplanner.service.spec.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    // private final MemberRepository memberRepository;

    @Override
    public void processOAuthLoginSuccess(MemberDTO member, HttpServletResponse response) {
        if(member.getEmail()==null) {
            // TODO : OAuth Resource Server 에서 가져오는 정보에 이메일 없는 경우 사용자가 수동으로 입력하도록 해야한다.
        } else {
            MemberDTO findMember = memberService.find(member.getEmail());

            if(findMember==null) {
                memberService.register(member);
                findMember = member;
            }

            setJwtCookie(findMember, response);
        }
    }

    @Override
    public void processFormLoginSuccess(MemberDTO member, HttpServletResponse response) {
        MemberDTO findMember = memberService.find(member.getEmail());
        boolean checkPw = passwordEncoder.matches(member.getPassword(), findMember.getPassword());

        if(findMember!=null && checkPw) {
            setJwtCookie(findMember, response);
        } else {
            // TODO : 잘못된 사용자 또는 비밀번호 입니다.
        }
    }

    private void setJwtCookie(MemberDTO member, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", member.getName());

        String jwt = jwtProvider.createToken(member.getEmail(), params);

        if(jwt!=null) {
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setPath("/");
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
        }
    }
}
