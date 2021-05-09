package com.springstudy.project.myweddingplanner.config;

import com.springstudy.project.myweddingplanner.security.jwt.JwtAuthenticationFilter;
import com.springstudy.project.myweddingplanner.security.jwt.JwtProvider;
import com.springstudy.project.myweddingplanner.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    final private JwtProvider jwtProvider;

    // authenticationManager를 Bean 등록합니다.
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                     // .antMatchers("/login", "/").permitAll()
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
        web.ignoring().antMatchers("/login", "/");
    }
}