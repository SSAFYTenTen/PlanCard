package com.ssafy.backend.domain.member.service;

import com.ssafy.backend.domain.member.dto.MemberLoginRequestDto;
import com.ssafy.backend.domain.member.dto.MemberSignUpRequestDto;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.exception.MemberError;
import com.ssafy.backend.domain.member.exception.MemberException;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
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
        if (memberRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new MemberException(MemberError.EXIST_MEMBER_EMAIL);
        }

        // Spring security 설정 시 PasswordEncoder 설정해야함
//        signUpRequestDto.setMemberPassword();
        memberRepository.save(signUpRequestDto.toEntity());
    }

    @Override
    public TokenMemberInfoDto loginCheckMember(MemberLoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()
        -> new MemberException(MemberError.NOT_FOUND_MEMBER));
        
        // Spring security 적용 시 비밀번호 암호화 로직 구현해야 함
        
        return TokenMemberInfoDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .role(member.getRole().toString())
                .build();
    }
}
