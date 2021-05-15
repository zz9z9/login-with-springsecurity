package com.springstudy.project.myweddingplanner.service.spec;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;
import com.springstudy.project.myweddingplanner.repository.entity.Member;

public interface MemberService {
    public MemberDTO find(String email);
    public String register(MemberDTO memberDTO);
}
