package com.example.swithme.jwt;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getTokenFromRequest(request);
        // 토큰이 존재하고, 로그인 메서드는 POST
        //  if ( StringUtils.hasText(tokenValue) && !req.getMethod().equals("GET") ) { // 토큰이 있고, GET 메서드가 아닐 경우
        if (StringUtils.hasText(tokenValue)) { // 토큰이 있고, GET 메서드가 아닐 경우

            tokenValue = jwtUtil.substringToken(tokenValue);

            if (!jwtUtil.validateToken(tokenValue)) {
                resultSetResponse(response,400,"유효하지 않은 토큰입니다.");
                log.error("유효하지 않은 토큰입니다.");
                return;
            }
            // 블랙리스트에 존재하는 토큰일 경우 조건문에 true 입력, 로그아웃된 토큰 메세지와  인증불가 코드 반환
            if (jwtUtil.isTokenBlacklisted(tokenValue)) {
                ApiResponseDto responseDto = new ApiResponseDto("로그아웃된 토큰입니다.", HttpStatus.UNAUTHORIZED.value());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(responseDto));
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            }catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request,response);
    }

    // 인증 처리 메서드
    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }


    // 인증 객세 생성 메서드
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

    // Client 에 반환할 msg, status 세팅 메서드
    private void resultSetResponse(HttpServletResponse res, int status, String msg) throws IOException {
        String jsonResult = " {\"status\": " + status + ", \"message\": \"" + msg + "\"}";

        // Content-Type 및 문자 인코딩 설정
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        // PrintWriter 를 사용하여 응답 데이터 전송
        PrintWriter writer = res.getWriter();
        writer.write(jsonResult);
        writer.flush();
    }
}