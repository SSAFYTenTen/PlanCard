package com.ssafy.backend.domain.member.service;

import com.ssafy.backend.domain.member.dto.MemberLoginRequestDto;
import com.ssafy.backend.domain.member.dto.MemberPasswordUpdateDto;
import com.ssafy.backend.domain.member.dto.MemberSignUpRequestDto;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.exception.MemberError;
import com.ssafy.backend.domain.member.exception.MemberException;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import com.ssafy.backend.global.component.jwt.repository.RefreshRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RefreshRepository refreshRepository;

    @Override
    public void signUpMember(MemberSignUpRequestDto signUpRequestDto) {
        if (memberRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new MemberException(MemberError.EXIST_MEMBER_EMAIL);
        }
        signUpRequestDto.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        memberRepository.save(signUpRequestDto.toEntity());
    }

    @Override
    public TokenMemberInfoDto loginCheckMember(MemberLoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()
        -> new MemberException(MemberError.NOT_FOUND_MEMBER));

        String realPassword = member.getPassword();

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), realPassword)) {
            throw new MemberException(MemberError.NOT_MATCH_PASSWORD);
        }
        
        return TokenMemberInfoDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .role(member.getRole().toString())
                .build();
    }

    @Override
    public void logoutMember(String email) {
        // 레디스에서 토큰 찾기
        Optional<String> token = refreshRepository.find(email);
        if (token.isEmpty()) {
            // 토큰이 존재하지 않는 경우 예외 발생
            throw new MemberException(MemberError.ALREADY_MEMBER_LOGOUT);
        }

        // 토큰이 존재하면 삭제
        refreshRepository.delete(email);
    }

    @Override
    public void updatePasswordMember(Long id, MemberPasswordUpdateDto passwordUpdateDto) {
        Member member = memberRepository.findById(id).orElseThrow(()
        -> new MemberException(MemberError.NOT_FOUND_MEMBER));

        String realPassword = member.getPassword();

        if(!passwordEncoder.matches(passwordUpdateDto.getNowPassword(), realPassword)) {
            throw new MemberException(MemberError.NOT_MATCH_PASSWORD);
        }

        if(passwordEncoder.matches(passwordUpdateDto.getChangePassword(), realPassword)) {
            throw new MemberException(MemberError.CURRENT_CHANGE_MATCH_PASSWORD);
        }

        member.updatePassword(passwordEncoder.encode(passwordUpdateDto.getChangePassword()));
    }


}
