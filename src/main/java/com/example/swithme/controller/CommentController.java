package com.example.swithme.controller;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.dto.CommentRequestDto;
import com.example.swithme.dto.CommentResponseDto;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/post/comment/{id}") @ResponseBody // id는 post id
    public ResponseEntity <ApiResponseDto> createComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDto responseDto = commentService.createComment(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);

    }
    // 댓글 조회
    @GetMapping("/post/comment/{id}") // id는 post id
    @ResponseBody
    public List<CommentResponseDto> commentList(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.commentList(id);
    }

    // 댓글 수정
    @PutMapping("/post/comment/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> updateComment (
            @PathVariable Long id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDto responseDto = commentService.updateComment(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    // 댓글 삭제
    @DeleteMapping("/post/comment/{id}") // id는 comment id
    @ResponseBody
    public ResponseEntity<ApiResponseDto> deleteComment (
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDto responseDto = commentService.deleteComment(id, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }
}
