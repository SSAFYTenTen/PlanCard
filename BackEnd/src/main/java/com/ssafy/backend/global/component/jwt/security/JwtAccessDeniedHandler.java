package com.ssafy.backend.global.component.jwt.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.backend.global.common.dto.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ssafy.backend.global.exception.GlobalError.NOT_AUTHORITY_MEMBER_API;

/**
 * 사용자가 필요한 권한 없이 접근을 시도할 때 처리하는 핸들러입니다.
 * 스프링 시큐리티의 AccessDeniedHandler 인터페이스를 구현합니다.
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 권한이 없는 사용자가 접근을 시도했을 때 처리하는 메서드입니다.
     * HTTP 상태 코드 403(Forbidden)으로 응답하고, 오류 메시지를 JSON 형식으로 반환합니다.
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param accessDeniedException 접근 거부 예외
     * @throws IOException 입출력 예외 발생 시
     * @throws ServletException 서블릿 예외 발생 시
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("권한없이 접근한 경우");
        setResponse(response);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(Message.fail(null, NOT_AUTHORITY_MEMBER_API.getErrorMessage())));
    }

    /**
     * HTTP 응답에 대한 설정을 지정하는 메서드입니다.
     * 상태 코드, 컨텐츠 유형 및 문자 인코딩을 설정합니다.
     *
     * @param response HTTP 응답
     */
    private void setResponse(HttpServletResponse response) {
        response.setStatus(NOT_AUTHORITY_MEMBER_API.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
    }
}
