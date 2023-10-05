package com.example.swithme.service;

import com.example.swithme.dto.user.GoogleUserInfoDto;
import com.example.swithme.entity.User;
import com.example.swithme.jwt.JwtUtil;
import com.example.swithme.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j(topic = "Google Login")
@Service
@RequiredArgsConstructor
public class GoogleService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate; // 수동 등록한 Bean
    private final JwtUtil jwtUtil;


    public String googleLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        log.info("googleLogin accessToken: " + accessToken);
        // 2. 토큰으로 구글 API 호출 : "액세스 토큰"으로 "구글 사용자 정보" 가져오기
        GoogleUserInfoDto googleUserInfoDto = googleUserInfoDto(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerGoogleUserIfNeeded(googleUserInfoDto);

        // 4. JWT 토큰 반환
        String createToken = jwtUtil.createToken(kakaoUser.getUsername());
        jwtUtil.addJwtToCookie(createToken,response);

        //return createToken;
        return createToken;
    }


    // 애플리케이션은 인증 코드로 구글 서버에 토큰을 요청하고, 토큰을 전달 받습니다.
    // 1) 액세스 토큰 요청 메서드
    public String getToken(String code) throws JsonProcessingException {
        log.info("getToken code: " + code);
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://oauth2.googleapis.com")
                .path("/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "964296755360-5db3hsu4jhn7dvtsbig72f4r6316obf0.apps.googleusercontent.com");
        body.add("client_secret", "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");
        body.add("redirect_uri","http://localhost:8080/api/users/google/callback"); // 애플리케이션 등록시 설정한 redirect_uri
        body.add("code",code); // 인가 코드

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri) // body 가 있으므로 post 메서드
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class // 반환값 타입은 String
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 값을 반환합니다.
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    // 2) 인가 토큰을 통해 사용자 정보 가져오기
    private GoogleUserInfoDto googleUserInfoDto(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfoDto> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                entity,
                GoogleUserInfoDto.class
        );

        GoogleUserInfoDto userInfo = null;

        if (response.getStatusCode() == HttpStatus.OK) {
            userInfo = response.getBody();

            System.out.println("User ID: " + userInfo.getId());
            System.out.println("Email: " + userInfo.getEmail());

        } else {
            System.out.println("Failed to fetch user info");
        }

        return userInfo;

    }


    // 3) 카카오 ID 정보로 회원가입
    private User registerGoogleUserIfNeeded(GoogleUserInfoDto googleUserInfoDto) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        String googleId = googleUserInfoDto.getId();
        User googleUser = userRepository.findByGoogleId(googleId).orElse(null);

        if (googleUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String googleEmail = googleUserInfoDto.getEmail();
            User sameEmailUser = userRepository.findByUsername(googleEmail).orElse(null);
            if (sameEmailUser != null) {
                googleUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                googleUser = googleUser.googleIdUpdate(googleId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = googleUserInfoDto.getEmail();

                //@앞에 글자만 가져와서 닉네임
                int location = email.indexOf("@");
                String nickname = email.substring(0, location);

                googleUser = new User(email, encodedPassword, nickname, googleId);
            }

            userRepository.save(googleUser);
        }

        return googleUser;
    }


}