package com.ssafy.backend.global.component.jwt.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.backend.global.common.dto.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ssafy.backend.global.exception.GlobalError.CERTIFICATION_NOT_TOKEN;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근하려 할 때 처리하는 엔트리 포인트입니다.
 * 스프링 시큐리티의 AuthenticationEntryPoint 인터페이스를 구현합니다.
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 인증되지 않은 사용자가 접근을 시도했을 때 처리하는 메서드입니다.
     * HTTP 상태 코드 401(Unauthorized)으로 응답하고, 오류 메시지를 JSON 형식으로 반환합니다.
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param authException 인증 예외
     * @throws IOException 입출력 예외 발생 시
     * @throws ServletException 서블릿 예외 발생 시
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("자격 증명 없이 접근한 경우");
        setResponse(response);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(Message.fail(null, CERTIFICATION_NOT_TOKEN.getErrorMessage())));
    }

    /**
     * HTTP 응답에 대한 설정을 지정하는 메서드입니다.
     * 상태 코드, 컨텐츠 유형 및 문자 인코딩을 설정합니다.
     *
     * @param response HTTP 응답
     */
    private void setResponse(HttpServletResponse response) {
        response.setStatus(CERTIFICATION_NOT_TOKEN.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
    }
}
