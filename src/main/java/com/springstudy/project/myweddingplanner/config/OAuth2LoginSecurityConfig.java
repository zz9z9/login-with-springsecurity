package com.springstudy.project.myweddingplanner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    private OidcUserService getgoogleUserService() {
        Set<String> googleScopes = new HashSet<>();
        googleScopes.add("https://www.googleapis.com/auth/userinfo.email");
        googleScopes.add("https://www.googleapis.com/auth/userinfo.profile");

        OidcUserService googleUserService = new OidcUserService();
        googleUserService.setAccessibleScopes(googleScopes);

        return googleUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/login","/").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .oauth2Login()
                    .loginPage("/login")
                    .defaultSuccessUrl("/member/main")
                    .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .and()
                .oauth2Login()
                    .userInfoEndpoint()
                    .oidcUserService(getgoogleUserService());
    }
}