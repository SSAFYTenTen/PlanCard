package com.ssafy.backend.global.component.jwt.security;

import com.ssafy.backend.domain.member.dto.MemberLoginActiveDto;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * JWT를 사용하여 인증하는데 필요한 토큰을 정의한 클래스입니다.
 */
@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final MemberLoginActiveDto principal;
    private final Object credentials;

    /**
     * JwtAuthenticationToken 생성자
     * @param principal 사용자 정보
     * @param credentials 인증 정보
     * @param authorities 권한 목록
     */
    public JwtAuthenticationToken(MemberLoginActiveDto principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // 고려사항: 인증 상태를 외부에서 설정할 수 있도록 변경
    }

    /**
     * 인증 과정에서 사용되는 자격 증명을 반환합니다.
     * 이 메서드는 스프링 시큐리티가 인증 과정에서 사용자의 자격 증명을 얻기 위해 호출합니다.
     * 자격 증명은 사용자의 비밀번호, 인증 토큰, 또는 기타 비밀 정보일 수 있습니다.
     *
     * @return 인증에 사용되는 자격 증명
     */
    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    /**
     * 인증 과정에서 식별되는 주체(사용자)의 정보를 반환합니다.
     * 이 메서드는 스프링 시큐리티가 인증하려는 주체의 정보를 얻기 위해 호출합니다.
     * 주체는 사용자 ID, 사용자 이름 또는 사용자 객체 자체일 수 있으며, 여기서는 MemberLoginActiveDto 객체가 주체의 역할을 합니다.
     *
     * @return 인증 주체에 대한 정보
     */
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

}
