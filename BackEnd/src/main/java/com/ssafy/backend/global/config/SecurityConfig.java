package com.ssafy.backend.global.config;


import com.ssafy.backend.global.component.jwt.security.JwtAccessDeniedHandler;
import com.ssafy.backend.global.component.jwt.security.JwtAuthenticationEntryPoint;
import com.ssafy.backend.global.component.jwt.security.JwtAuthenticationFilter;
import com.ssafy.backend.global.component.jwt.service.JwtService;
import com.ssafy.backend.global.exception.ExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity  // Spring Security를 활성화하기 위한 어노테이션입니다.
@RequiredArgsConstructor    // Lombok을 사용하여 생성자 주입을 자동으로 구현합니다.
@EnableMethodSecurity(securedEnabled = true,prePostEnabled = true)  // 메서드 수준의 보안을 활성화 합니다.
public class SecurityConfig {

    // 의존성 주입을 위한 필드 선언
    private final JwtService jwtService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Spring Security의 주요 구성을 담당하는 메서드입니다.
     * HttpSecurity 객체를 통해 HTTP 요청에 대한 보안을 구성합니다.
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain 객체
     * @throws Exception 보안 구성 중 발생하는 예외를 처리합니다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Spring Security 6.1.0 부터는 메서드 체이닝의 사용을 지양하고 람다식을 통해 함수형으로 설정 지향함
        // HTTP 보안 설정
        http.authorizeHttpRequests((authorizeRequests) ->
                authorizeRequests
                        .anyRequest().permitAll()); // 모든 요청에 대해 접근을 허용합니다.

        // H2-Console과 같은 프레임을 활용하는 리소스의 사용을 위해 X-Frame-Options를 SAMEORIGIN으로 설정
        http.headers((headerConfig) ->
                headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // 세션을 사용하지 않기 때문에 STATELESS로 세션 정책을 설정합니다.
        // 이는 JWT를 사용하기 때문에 필요한 설정입니다.
        http.sessionManagement((sessionManage) ->
                sessionManage.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CORS 설정을 적용합니다. 모든 출처, 메서드, 헤더에 대해 요청을 허용합니다.
        http.cors((corsConfigurer) ->
                        corsConfigurer.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 로그인 방식을 사용하지 않습니다.
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 기능을 비활성화합니다.
                .sessionManagement((sessionManagementConfigurer) ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 폼 기반 로그인과 로그아웃을 비활성화합니다.
        http.formLogin(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);

        // 예외 처리 핸들러를 설정합니다. 인증 실패와 권한 부족 상황을 처리합니다.
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler));

        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 등록합니다.
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // ExceptionHandlerFilter를 JwtAuthenticationFilter 전에 등록합니다.
        http.addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationFilter.class);



        return http.build();
    }

    /**
     * CORS(Cross-Origin Resource Sharing)에 대한 구성을 제공하는 메서드입니다.
     * 이 설정을 통해 다양한 출처(origin)의 요청을 안전하게 처리할 수 있습니다.
     *
     * @return CORS 구성을 위한 CorsConfigurationSource 객체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // CORS 구성을 위한 CorsConfiguration 객체 생성
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // 모든 출처의 요청을 허용합니다.
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 (GET, POST 등)를 허용합니다.
        configuration.addAllowedHeader("*"); // 모든 요청 헤더를 허용합니다.
        configuration.setAllowCredentials(true); // 크레덴셜(인증정보)을 포함한 요청을 허용합니다.

        // URL 기반으로 CORS 구성을 설정합니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위에서 정의한 CORS 구성을 적용합니다.
        return source;
    }

    /**
     * 패스워드 암호화를 위한 PasswordEncoder를 빈으로 등록합니다.
     * BCrypt 알고리즘을 사용하여 패스워드를 암호화합니다.
     *
     * @return PasswordEncoder의 구현체인 BCryptPasswordEncoder 객체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 패스워드 암호화 빈 설정
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT 인증을 처리하는 필터를 빈으로 등록합니다.
     * JwtService를 사용하여 토큰의 유효성을 검증합니다.
     *
     * @return JwtAuthenticationFilter 객체
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        // JWT 인증 필터 빈 설정
        return new JwtAuthenticationFilter(jwtService);
    }

    /**
     * 예외 처리를 담당하는 필터를 빈으로 등록합니다.
     * 애플리케이션에서 발생하는 예외를 적절히 처리합니다.
     *
     * @return ExceptionHandlerFilter 객체
     */
    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter() {
        // 예외 처리 필터 빈 설정
        return new ExceptionHandlerFilter();
    }
}
