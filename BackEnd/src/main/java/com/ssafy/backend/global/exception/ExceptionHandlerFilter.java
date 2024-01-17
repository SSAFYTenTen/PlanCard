package com.ssafy.backend.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.backend.global.common.dto.Message;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * HTTP 요청 처리 중 발생하는 특정 예외를 처리하는 필터입니다.
 * 이 필터는 스프링의 OncePerRequestFilter를 상속받아, 각 요청에 대해 정확히 한 번씩 호출됩니다.
 */
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    /**
     * 각 HTTP 요청에 대해 필터링을 수행합니다.
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
        try {
            // 필터 체인을 계속 진행합니다. 여기서 다음 필터로 요청이 전달됩니다.
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            // TokenException이 발생할 경우, 사용자에게 에러 응답을 설정합니다.
            setErrorResponse(response, e.getErrorCode(), request);
        }
    }

    /**
     * 에러 발생 시 HTTP 응답을 설정하는 메서드입니다.
     * 에러 코드에 따라 상태 코드, 컨텐츠 유형 및 에러 메시지를 설정합니다.
     *
     * @param response HTTP 응답
     * @param errorCode 발생한 에러의 코드
     * @param request HTTP 요청
     */
    private void setErrorResponse(HttpServletResponse response, GlobalError errorCode, HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatus().value()); // HTTP 상태 코드 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 컨텐츠 유형 설정
        response.setCharacterEncoding("utf-8"); // 문자 인코딩 설정 (한글 깨짐 방지)

        try {
            // 사용자에게 전달될 에러 메시지를 JSON 형태로 작성하여 응답에 씁니다.
            response.getWriter().write(objectMapper.writeValueAsString(Message.fail(errorCode.toString(), errorCode.getErrorMessage())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
