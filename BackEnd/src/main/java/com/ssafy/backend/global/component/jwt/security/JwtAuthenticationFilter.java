package com.ssafy.backend.global.component.jwt.security;

import com.ssafy.backend.domain.member.dto.MemberLoginActiveDto;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import com.ssafy.backend.global.component.jwt.service.JwtService;
import com.ssafy.backend.global.exception.GlobalError;
import com.ssafy.backend.global.exception.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * JWT 기반의 인증을 처리하는 필터입니다.
 * 이 필터는 HTTP 요청이 들어올 때마다 JWT 토큰을 검증하고,
 * 인증 정보를 스프링 시큐리티의 보안 컨텍스트에 저장합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;


    /**
     * 각 HTTP 요청에 대해 정확히 한 번씩 호출되어 필터링을 수행합니다.
     * 요청 헤더에서 JWT 토큰을 추출하고, 이를 검증하여 인증 정보를 설정합니다.
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // HTTP 요청에서 JWT 토큰을 추출합니다.
        String jwt = getJwtToken(request);
        // 추출된 토큰을 사용하여 사용자를 인증합니다.
        authenticate(request, jwt);
        // 필터 체인을 계속 진행합니다. 이는 다음 필터로 요청을 전달하거나 필터 체인의 끝에 도달했다면 요청 처리를 계속 진행합니다.
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청의 헤더에서 JWT 토큰을 추출합니다.
     * "Authorization" 헤더에서 "Bearer " 접두사를 가진 토큰을 찾아 반환합니다.
     *
     * @param request HTTP 요청
     * @return 추출된 JWT 토큰, 없을 경우 null
     */
    private String getJwtToken(HttpServletRequest request) {
        // "Authorization" 헤더를 추출합니다.
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // 헤더가 "Bearer "로 시작하는 경우에만 토큰을 반환합니다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7); // "Bearer " 이후의 문자열을 토큰으로 반환합니다.
        }
        return null; // 토큰이 없는 경우 null을 반환합니다.
    }


    /**
     * 추출된 JWT 토큰을 사용하여 사용자 인증을 시도합니다.
     * 토큰이 유효하면 사용자 정보를 파싱하여 보안 컨텍스트에 저장합니다.
     * 토큰이 유효하지 않을 경우 보안 컨텍스트를 클리어하고 예외를 던집니다.
     *
     * @param request HTTP 요청
     * @param token JWT 토큰
     */
    private void authenticate(HttpServletRequest request, String token) {
        TokenMemberInfoDto tokenMemberInfoDto = null;
        // 토큰이 있을 경우에만 처리를 진행합니다.
        if (StringUtils.hasText(token)) {
            try {
                // JWT 서비스를 사용하여 토큰에서 사용자 정보를 파싱합니다.
                tokenMemberInfoDto = jwtService.parseAccessToken(token);
                // 파싱된 사용자 정보를 바탕으로 로그인 활성 정보 객체를 생성합니다.
                MemberLoginActiveDto loginActiveDto = MemberLoginActiveDto.from(tokenMemberInfoDto);
                // 생성된 정보를 보안 컨텍스트에 저장합니다.
                saveLoginMemberInSecurityContext(loginActiveDto);
            } catch(RuntimeException e) {
                // 토큰 파싱 중 오류가 발생하면 보안 컨텍스트를 클리어하고 예외를 던집니다.
                SecurityContextHolder.clearContext();
                throw new TokenException(GlobalError.INVALID_TOKEN);
            }
        }
    }

    /**
     * 사용자의 인증 정보를 스프링 시큐리티의 보안 컨텍스트에 저장합니다.
     * JwtAuthenticationToken 객체를 생성하여 SecurityContextHolder에 설정합니다.
     *
     * @param memberLoginActiveDto 사용자의 로그인 활성 정보
     */
    private static void saveLoginMemberInSecurityContext(MemberLoginActiveDto memberLoginActiveDto) {
        // 사용자 정보와 역할을 기반으로 JwtAuthenticationToken 객체를 생성합니다.
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                memberLoginActiveDto, "", Arrays.asList(new SimpleGrantedAuthority(memberLoginActiveDto.getRole()))
        );
        // 생성된 인증 객체를 SecurityContextHolder에 설정합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
