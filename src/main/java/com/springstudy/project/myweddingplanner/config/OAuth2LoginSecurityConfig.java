package com.springstudy.project.myweddingplanner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/").permitAll()
                .anyRequest().authenticated()
                .and()
                    .oauth2Login()
                        .loginPage("/login")
                            .defaultSuccessUrl("/member/main")
                .and()
                    .logout()
                        .logoutSuccessUrl("/");
    }
}