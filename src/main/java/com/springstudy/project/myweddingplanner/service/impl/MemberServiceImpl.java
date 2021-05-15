package com.springstudy.project.myweddingplanner.service.impl;

import com.springstudy.project.myweddingplanner.dto.MemberDTO;
import com.springstudy.project.myweddingplanner.repository.MemberRepository;
import com.springstudy.project.myweddingplanner.repository.entity.Member;
import com.springstudy.project.myweddingplanner.service.spec.MemberService;
import com.springstudy.project.myweddingplanner.util.EntityDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public MemberDTO find(String email) {
        Member member = memberRepository.findById(email).orElseGet(null);
        return EntityDtoConverter.convertEntityToDto(member, new MemberDTO());
    }

    @Override
    public String register(MemberDTO memberDTO) {
        Member member = new Member();
        String encodedPw = passwordEncoder.encode(memberDTO.getPassword());
        memberDTO.setPassword(encodedPw);

        BeanUtils.copyProperties(memberDTO, member);

        memberRepository.save(member);

        return member.getEmail();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findById(email).orElseGet(Member::new);
    }
}
