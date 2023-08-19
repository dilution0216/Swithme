package com.example.swithme.controller;

import com.example.swithme.dto.LoginRequestDto;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.security.UserDetailsServiceImpl;
import com.example.swithme.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserViewController {

    private final UserService userService;

    @GetMapping("/users/login")
    public String login() {
        return "user/login";
    }

    @ResponseBody
    @GetMapping("/board")
    public String board(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();

        return username;
    }


}