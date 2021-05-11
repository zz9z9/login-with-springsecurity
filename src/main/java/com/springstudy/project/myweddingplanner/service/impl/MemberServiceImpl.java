package com.springstudy.project.myweddingplanner.service.impl;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;
import com.springstudy.project.myweddingplanner.repository.MemberRepository;
import com.springstudy.project.myweddingplanner.repository.entity.Member;
import com.springstudy.project.myweddingplanner.service.spec.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public String register(MemberDTO memberDTO) {
        Member member = new Member();
        String encodedPw = passwordEncoder.encode(memberDTO.getPassword());
        memberDTO.setPassword(encodedPw);

        BeanUtils.copyProperties(memberDTO, member);

        memberRepository.save(member);

        return member.getEmail();
    }
}