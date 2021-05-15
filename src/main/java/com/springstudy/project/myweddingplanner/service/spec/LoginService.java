package com.springstudy.project.myweddingplanner.service.spec;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    public void processOAuthLoginSuccess(MemberDTO member, HttpServletResponse response);

    public void processFormLoginSuccess(MemberDTO member, HttpServletResponse response);
}
