package com.example.swithme.controller;

import com.example.swithme.dto.AccumulatedTimeDto;
import com.example.swithme.dto.user.*;
import com.example.swithme.entity.User;
import com.example.swithme.jwt.JwtUtil;
import com.example.swithme.repository.AccumulatedTimeRepository;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.GoogleService;
import com.example.swithme.service.KakaoService;
import com.example.swithme.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final JwtUtil jwtUtil;
    private final AccumulatedTimeRepository accumulatedTimeRepository;

    // 회원가입, 로그인 페이지 이동
    @GetMapping("/users/login")
    public String loginPage(Model model, HttpServletRequest request) {
        model.addAttribute("signupRequestDto", new SignupRequestDto());

        return "user/login";
    }

    //회원가입
    @PostMapping("/api/users/signup")
    public String signup(@Validated @ModelAttribute SignupRequestDto signupRequestDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        //검증 오류 확인 후 에러 발생시 리턴
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "user/signUp";
        }
        //특정 조건 오류 검증 후 실패시 리턴
        userService.signup(signupRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/signUp";
        }

        return "redirect:/users/login?content=email";

    }

    //이메일 인증 요청
    @GetMapping("/api/users/email/{mail}")
    public String authenticateEmail(@PathVariable String mail) {
            userService.authenticateEmail(mail);
        return "redirect:/users/login";
    }

    //일반 로그인
    @PostMapping("/api/users/login")
    public String login(@ModelAttribute LoginRequestDto requestDto, HttpServletResponse response) {

        // 로그인 실패시
        try {
            userService.login(requestDto,response);
        } catch (IllegalArgumentException e) {
            e.getMessage();
            return "redirect:/users/login?content=loginFail";
        }

            return "redirect:/";
    }

    //카카오 로그인
    @GetMapping("/api/users/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoService.kakaoLogin(code, response); // 쿠키 생성

            return "redirect:/";
    }

    //구글 로그인
    @GetMapping("/api/users/google/callback")
    public String googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        googleService.googleLogin(code, response); // 반환 값이 JWT 토큰

            return "redirect:/";
    }

    //로그아웃
    @GetMapping("/users/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String tokenValue = jwtUtil.getTokenFromRequest(request);
        userService.logout(tokenValue);
        jwtUtil.expireCookie(response);
        return "redirect:/";
    }

    //마이 페이지 이동
    @GetMapping("/users/myPage")
    public String myPage(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        if((user.getGoogleId() == null && user.getKakaoId() == null)) {
            model.addAttribute("userUpdateRequestDto", new UserUpdateRequestDto(user));
            return "user/myPage";
        }
        model.addAttribute("userUpdateRequestDto", new UserUpdateRequestDto(user));
        return "user/myPageOauth2Login";
    }

    //일반 로그인 정보 수정(닉네임)
    @PostMapping("/api/users/myPage")
    public String updateUser(@Validated @ModelAttribute UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "user/myPage";
        }

        UserUpdateResponseDto userUpdateResponseDto = userService.updateUser(userUpdateRequestDto);
        model.addAttribute("userUpdateRequestDto", userUpdateResponseDto);
        model.addAttribute("message", "닉네임이 수정되었습니다.");
        return "user/myPage";
    }

    //구글, 카카오 로그인 정보 수정(닉네임)
    @PostMapping("/api/users/Oauth2myPage")
    public String updateOauth2User(@Validated @ModelAttribute UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "user/myPageOauth2Login";
        }

        UserUpdateResponseDto userUpdateResponseDto = userService.updateUser(userUpdateRequestDto);
        model.addAttribute("userUpdateRequestDto", userUpdateResponseDto);
        model.addAttribute("message", "닉네임이 수정되었습니다.");
        return "user/myPageOauth2Login";
    }

    //일반 로그인 비밀번호 변경 페이지 이동
    @GetMapping("/users/updatePassword")
    public String updateMovePassword(Model model) {
        model.addAttribute("passwordRequestDto", new PasswordRequestDto());
        return "user/updatePassword";
    }

    //비밀번호 변경
    @PostMapping("/api/users/updatePassword")
    public String updatePassword(@Validated @ModelAttribute PasswordRequestDto passwordRequestDto, BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "user/updatePassword";
        }
        userService.updatePassword(passwordRequestDto, userDetails.getUser(), bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/updatePassword";
        }
        return "redirect:/?success=password";
    }

    //회원탈퇴 페이지로 이동
    @GetMapping("/users/delete")
    public String moveDeleteUser(@AuthenticationPrincipal  UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        model.addAttribute("username", user.getUsername());
        return "user/deleteUser";
    }

    //회원탈퇴
    @PostMapping("/api/users/delete")
    public String deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute PasswordRequestDto passwordRequestDto, Model model, HttpServletResponse response) {
        User user = userDetails.getUser();
        if (userService.checkPassword(user, passwordRequestDto)) {
            userService.delete(user);
            jwtUtil.expireCookie(response);
            return "redirect:/?success=delete";
        }
        model.addAttribute("username", user.getUsername());
        model.addAttribute("error", "비밀번호를 확인해주세요");
        return "user/deleteUser";
    }

    @GetMapping("/user/accumulated_id")
    public String getaccumulatedtime() {

        return null;
    }

    // 특정 사용자의 누적 시간 조회 엔드포인트
    // 이것도 서비스단으로 내려야함
    // 하지만 RecordController 에서 Get매핑으로 조회할땐 서비스단에서 구성하여 호출하기
    // 이 부분을 RecordService 로 만들어서 Record 컨트롤러에서 조회로 사용하기
    @GetMapping("/{userId}/accumulated-time")
    public ResponseEntity<AccumulatedTimeDto> getAccumulatedTime(@PathVariable Long userId) {
        try {
            // UserService를 사용하여 특정 사용자의 누적 시간을 조회
            AccumulatedTimeDto accumulatedTimeDto = userService.getAccumulatedTimeByUserId(userId);
            return ResponseEntity.ok(accumulatedTimeDto);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}