package com.springstudy.project.myweddingplanner.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    // private final AuthenticationManager authenticationManager = new ProviderManager();

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("JwtAuthenticationFilter.doFilter");
        String token = jwtProvider.resolveToken((HttpServletRequest) request); // 헤더에서 JWT 를 받아옵니다.

        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token); // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext 에 Authentication 객체를 저장합니다.

            System.out.println("authentication : "+authentication);
        }

        chain.doFilter(request, response);
    }
}