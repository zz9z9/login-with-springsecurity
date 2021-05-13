package com.springstudy.project.myweddingplanner.config;

import com.springstudy.project.myweddingplanner.security.jwt.JwtAuthenticationFilter;
import com.springstudy.project.myweddingplanner.security.jwt.JwtProvider;
import com.springstudy.project.myweddingplanner.security.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    final private JwtProvider jwtProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                    .disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
                .csrf()
                    .disable() // csrf 보안 토큰 disable처리.
//                .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
//                .and()
                .authorizeRequests()
                    .antMatchers("/main/**").authenticated()
                    .anyRequest().permitAll()
                .and()
                    .oauth2Login()
                        .loginPage("/login")
                            .userInfoEndpoint()
                                .userService(new CustomOAuth2UserService())
                .and()
                    .defaultSuccessUrl("/login/oauth/success")
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
         web.ignoring().antMatchers("/login", "/member/signup","/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}