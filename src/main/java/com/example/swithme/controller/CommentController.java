package com.example.swithme.controller;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.dto.CommentRequestDto;
import com.example.swithme.dto.CommentResponseDto;
import com.example.swithme.exception.TokenNotValidateException;
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
    @PostMapping("/myStudy/comment/{id}") @ResponseBody // id는 myStudy id
    public ResponseEntity <ApiResponseDto> createComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        ApiResponseDto responseDto = commentService.createComment(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);

    }
    // 댓글 조회
    @GetMapping("/myStudy/comment/{id}") // id는 user id
    @ResponseBody
    public List<CommentResponseDto> commentList(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.commentList(id);
        // @AuthenticationPrincipal UserDetailsImpl userDetails 를 사용한 이유?
    }

    // 댓글 수정
    @PutMapping("/myStudy/comment/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> updateComment (
            @PathVariable Long id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        ApiResponseDto responseDto = commentService.updateComment(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    // 댓글 삭제
    @DeleteMapping("/myStudy/comment/{id}") // id는 comment id
    @ResponseBody
    public ResponseEntity<ApiResponseDto> deleteComment (
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        ApiResponseDto responseDto = commentService.deleteComment(id, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }
    public void tokenValidate(UserDetailsImpl userDetails) {
        try{
            userDetails.getUser();
        }catch (Exception ex){
            throw new TokenNotValidateException("토큰이 유효하지 않습니다.");
        }
    }
}