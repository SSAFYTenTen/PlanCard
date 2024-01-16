package com.ssafy.backend.domain.member.service;

import com.ssafy.backend.domain.member.dto.MemberSignUpRequestDto;
import com.ssafy.backend.domain.member.exception.MemberError;
import com.ssafy.backend.domain.member.exception.MemberException;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void signUpMember(MemberSignUpRequestDto signUpRequestDto) {
        if (memberRepository.existsByEmail(signUpRequestDto.getMemberEmail())) {
            throw new MemberException(MemberError.EXIST_MEMBER_EMAIL);
        }

        // Spring security 설정 시 PasswordEncoder 설정해야함
//        signUpRequestDto.setMemberPassword();
        memberRepository.save(signUpRequestDto.toEntity());
    }
}
