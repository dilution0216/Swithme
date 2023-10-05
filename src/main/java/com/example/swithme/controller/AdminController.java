package com.example.swithme.controller;

import com.example.swithme.enumType.UserRole;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Secured(UserRole.Authority.ADMIN) // 관리자만 접근 가능
public class AdminController {

    AdminService adminService;

    @GetMapping("/secured")
    public String getProductsByAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            System.out.println("authority.getAuthority() = " + authority.getAuthority());
        }
        return "user/admin";
    }

    // 전체 개인 스터디 게시물 조회
    @GetMapping("/secured/myStudies")
    public String getMyStudies() {
        adminService.findAll();
        return "user/admin";
    }
}
