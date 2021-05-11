package com.springstudy.project.myweddingplanner.service.spec;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;
import com.springstudy.project.myweddingplanner.repository.entity.Member;

public interface MemberService {
    public String register(MemberDTO memberDTO);
}
