package com.ssafy.backend.global.config;


import com.ssafy.backend.global.component.jwt.service.JwtService;
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
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

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
                        .anyRequest().permitAll());

        http.headers((headerConfig) ->
                headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));   //

        http.sessionManagement((sessionManage) ->
                sessionManage.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.cors((corsConfigurer) ->
                        corsConfigurer.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagementConfigurer) ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.formLogin(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);

//        http.exceptionHandling((exceptionHandling) ->
//                exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                        .accessDeniedHandler(jwtAccessDeniedHandler));
//
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationFilter.class);



        return http.build();
    }

    /**
     * CORS(Cross-Origin Resource Sharing)에 대한 구성을 제공하는 메서드입니다.
     * CORS 정책을 설정하여 다양한 출처의 요청을 처리할 수 있습니다.
     *
     * @return CORS 구성을 위한 CorsConfigurationSource 객체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // CORS 관련 설정
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
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
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        // JWT 인증 필터 빈 설정
//        return new JwtAuthenticationFilter(jwtService);
//    }

    /**
     * 예외 처리를 담당하는 필터를 빈으로 등록합니다.
     * 애플리케이션에서 발생하는 예외를 적절히 처리합니다.
     *
     * @return ExceptionHandlerFilter 객체
     */
//    @Bean
//    public ExceptionHandlerFilter exceptionHandlerFilter() {
//        // 예외 처리 필터 빈 설정
//        return new ExceptionHandlerFilter();
//    }
}
