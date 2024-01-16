package com.ssafy.backend.domain.member.repository;

import com.ssafy.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 기본적인 CRUD는 JPA에서 제공

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
