package com.example.swithme.service;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.dto.CategoryResponseDto;
import com.example.swithme.entity.Category;
import com.example.swithme.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 게시물 내 카테고리 주제 조회
    public ResponseEntity<ApiResponseDto> getCategories() {
        List<Category> categoryList = categoryRepository.findAllByOrderByIdAsc();
        List<CategoryResponseDto> newCategoryList = categoryList.stream().map(CategoryResponseDto::new).toList();

        return ResponseEntity.status(200).body(new ApiResponseDto(HttpStatus.OK.value(), "전체 카테고리 조회 성공", newCategoryList));
    }
}