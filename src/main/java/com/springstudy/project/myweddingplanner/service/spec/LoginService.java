package com.springstudy.project.myweddingplanner.service.spec;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    public void processWhenLoginSuccess(MemberDTO member, HttpServletResponse response);
}
