package com.springstudy.project.myweddingplanner.repository;

import com.springstudy.project.myweddingplanner.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
}
