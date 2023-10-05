package com.example.swithme.controller;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    // 게시물 내 카테고리 주제 조회
    @GetMapping("/categories")
    public ResponseEntity<ApiResponseDto> getCategories() {
        return categoryService.getCategories();
    }
}