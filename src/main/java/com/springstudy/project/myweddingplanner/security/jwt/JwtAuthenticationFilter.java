package com.springstudy.project.myweddingplanner.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;
    // private final AuthenticationManager authenticationManager = new ProviderManager();

    public JwtAuthenticationFilter(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtAuthenticationFilter.doFilter");
        String accessToken = jwtManager.getTokenByKey(request, JwtManager.ACCESS_TOKEN_NAME);
        String refreshToken = jwtManager.getTokenByKey(request, JwtManager.REFRESH_TOKEN_NAME);

        // accessToken 유효한지 확인
        if(accessToken!=null) {
            if(jwtManager.isNotExpired(accessToken)) {
                Authentication authentication = jwtManager.getAuthentication(accessToken); // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext 에 Authentication 객체를 저장합니다.
            } else { // accessToken 만료되었으면 refreshToken 확인
                if(jwtManager.isNotExpired(refreshToken)) {
                    String userIdentifier = jwtManager.getUserIdentifier(accessToken);
                    String newAccessToken = jwtManager.generateAccessToken(userIdentifier, new HashMap<>());

                    setCookie(response, JwtManager.ACCESS_TOKEN_NAME, newAccessToken);

                    Authentication authentication = jwtManager.getAuthentication(newAccessToken); // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                    SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext 에 Authentication 객체를 저장합니다.
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}