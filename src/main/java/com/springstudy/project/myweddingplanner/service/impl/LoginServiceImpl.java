package com.springstudy.project.myweddingplanner.service.impl;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;
import com.springstudy.project.myweddingplanner.security.jwt.JwtManager;
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
    private final JwtManager jwtManager;
    private final MemberService memberService;

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

            Map<String, Object> params = new HashMap<>();
            params.put("name", findMember.getName());

            String accessToken = jwtManager.generateAccessToken(findMember.getEmail(), params);
            String refreshToken = jwtManager.generateRefreshToken();

            setCookie(response, "accessToken", accessToken);
            setCookie(response, "refreshToken", refreshToken);

            jwtManager.saveRefreshTokenInStorage(findMember.getEmail(), refreshToken);
        }
    }

    @Override
    public void processFormLoginSuccess(MemberDTO member, HttpServletResponse response) {
        MemberDTO findMember = memberService.find(member.getEmail());
        boolean checkPw = passwordEncoder.matches(member.getPassword(), findMember.getPassword());

        if(findMember!=null && checkPw) {
            Map<String, Object> params = new HashMap<>();
            params.put("name", member.getName());

            String accessToken = jwtManager.generateAccessToken(member.getEmail(), params);
            String refreshToken = jwtManager.generateRefreshToken();

            setCookie(response, "accessToken", accessToken);
            setCookie(response, "refreshToken", refreshToken);

            jwtManager.saveRefreshTokenInStorage(findMember.getEmail(), refreshToken);
        } else {
            // TODO : 잘못된 사용자 또는 비밀번호 입니다.
            response.setStatus(500);
        }
    }

    private void setCookie(HttpServletResponse response, String key, String value) {
         Cookie cookie = new Cookie(key, value);
         cookie.setPath("/");
         cookie.setHttpOnly(true);
         response.addCookie(cookie);
    }

}
