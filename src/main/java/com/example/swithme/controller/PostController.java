package com.example.swithme.controller;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.dto.PostRequestDto;
import com.example.swithme.dto.PostResponseDto;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시글 생성
    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> createPost(
            @RequestPart("data") PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        ApiResponseDto responseDto = postService.createPost(requestDto, userDetails.getUser(), image);
        return ResponseEntity.ok().body(responseDto);
    }
    // 전체 게시물 조회
    @GetMapping("/posts")
    @ResponseBody
    public ResponseEntity<List<PostResponseDto>> getPosts() {
         List<PostResponseDto> responseDto = postService.getPosts();
         return ResponseEntity.ok().body(responseDto);
    }

    // 게시물 단건 조회
    @GetMapping("/post/{id}")
    @ResponseBody
    public ResponseEntity<PostResponseDto> lookupPost(@PathVariable Long id) {
        PostResponseDto responseDto = postService.lookupPost(id);
        return ResponseEntity.ok().body(responseDto);

    }

    // 게시물 수정
    @PutMapping("/post/{id}")
    public ResponseEntity<ApiResponseDto> updatePost(
            @PathVariable Long id,
            @RequestPart("data") PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return postService.updatePost(id, postRequestDto, userDetails.getUser(), image);
    }

    // 게시물 삭제
    @DeleteMapping("/post/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getUser());
    }

    // 카테고리별 게시물 조회
    @GetMapping("/posts/category/{category_id}")
    public ResponseEntity<ApiResponseDto> getCategoryPosts(@PathVariable Long category_id) {
        return postService.getCategoryPosts(category_id);
    }
}
