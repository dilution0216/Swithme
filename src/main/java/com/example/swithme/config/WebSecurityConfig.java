package com.example.swithme.config;

import com.example.swithme.jwt.JwtAuthorizationFilter;
import com.example.swithme.jwt.JwtUtil;
import com.example.swithme.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        //모두 접근 허용된 URL
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/").permitAll() //메인페이지 접근 허용
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll() // 로그인, 회원가입 누구나 가능.
                        .requestMatchers("/api/users/**").permitAll() // 로그인, 회원가입 누구나 가능.
                        .requestMatchers("chat/content").permitAll()

                        //로그인 필요
                        // .requestMatchers("/view/post/write").authenticated()
                        .requestMatchers("view/post/**").authenticated() //게시글
                        .requestMatchers("view/post/detail/**").authenticated() //게시글
                        .requestMatchers("/chat/**").authenticated() // 개인채팅
                        .requestMatchers("/studies/calendar").authenticated() //캘린더
                        .requestMatchers("/stopwatch").authenticated() //스탑워치
                        .requestMatchers("/api/view/groups").authenticated() //그룹스터디

                        //그 외 모든 접근 허용
                        .anyRequest().permitAll()
        );

        // 인증필요한 페이지 이동시 기본 로그인 페이지
        http.formLogin((formLogin) ->
                formLogin
                        .loginPage("/users/login").permitAll()
        );

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}